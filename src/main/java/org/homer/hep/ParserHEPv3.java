/*
 * $Id$
 *
 *  HEP java parser 
 *
 *  Author: Alexandr Dubovikov <alexandr.dubovikov@gmail.com>
 *  (C) Homer Project 2012-2014 (http://www.sipcapture.org)
 *
 * Homer capture agent is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version
 *
 * Homer capture agent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
*/

/*
 * Changes (fixes) made by Vadim:
 *  * Removed dead code (there was a lot of it)
 *  * Added Slf4j logger
 *  * Flip payload buffer after data was written to it
 *  * Replaced old switch case with modern version
 *  * Removed useless "source host" method argument
 */

package org.homer.hep;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@Slf4j
public class ParserHEPv3 {

  	public static final int WIDTH = 4;

    /**
     * Method to parse out HEPStructure from input byte buffer.
     *
     * @param msg             payload bytes
     * @return HEPStructure instance
     * @throws Exception if failed to parse the HEP message
     */
	public HEPStructure parse(final ByteBuffer msg) throws Exception {
		final var header = new byte[4];
		msg.get(header); // read the header
		if (!new String(header).equals("HEP3"))
			throw new IOException("Message doesn't have 'HEP3' header");
		
		final var totalLength = Short.toUnsignedInt(msg.getShort());
		
		final var hepBuilder = HEPStructure.builder();
		try {
			int chunk_id = 0;
			int chunk_type = 0;
			int chunk_length = 0;
			int i = 6;			

			while (i < totalLength) {
				chunk_id = msg.getShort();
				chunk_type = msg.getShort();
				chunk_length = msg.getShort();
				
				if(chunk_length > totalLength) {
					log.error("Corrupted HEP: CHUNK LENGTH couldn't be bigger as CHUNK_LENGTH");
                    throw new IOException("Invalid HEP payload.");
				}
				
				if(chunk_length == 0) {
					log.error("Corrupted HEP: LENGTH couldn't be 0!");
                    throw new IOException("Invalid HEP payload.");
				}

				/* working with based HEP */
				if (chunk_id != 0) {
					msg.position(msg.position() + chunk_length - 6);					
					continue;
				}

				switch (chunk_type) {
					case 1 -> hepBuilder.ipFamily(msg.get());
					case 2 -> hepBuilder.protocolId(msg.get());
					case 3 -> hepBuilder.sourceIPAddress(readIPv4(msg));
					case 4 -> hepBuilder.destinationIPAddress(readIPv4(msg));
					case 5 -> hepBuilder.sourceIPAddress(readIPv6(msg));
					case 6 -> hepBuilder.destinationIPAddress(readIPv6(msg));
					case 7 -> hepBuilder.sourcePort(Short.toUnsignedInt(msg.getShort()));
					case 8 -> hepBuilder.destinationPort(Short.toUnsignedInt(msg.getShort()));
					case 9 -> hepBuilder.timeSeconds(Integer.toUnsignedLong(msg.getInt()));
					case 10 -> hepBuilder.timeUseconds(Integer.toUnsignedLong(msg.getInt()));
					case 11 -> hepBuilder.protocolType(msg.get());
					case 12 -> hepBuilder.captureId(msg.getInt());
					case 14 -> {
						hepBuilder.captureAuthUser(new String(msg.array(), msg.position(), (chunk_length - 6)));
						msg.position(msg.position() + chunk_length - 6);
					}
					case 15 -> {
						final var buffer = ByteBuffer.allocate((chunk_length - 6));
						buffer.put(msg.array(), msg.position(), (chunk_length - 6));
						buffer.flip();
						hepBuilder.payloadByteMessage(buffer);
						msg.position(msg.position() + chunk_length - 6);
					}
					case 16 -> {
						/* compressed payload */
						final var buffer = ByteBuffer.wrap(extractBytes(msg.array(), msg.position(), (chunk_length - 6)));
						buffer.flip();
						hepBuilder.payloadByteMessage(buffer);
						msg.position(msg.position() + chunk_length - 6);
					}
					case 17 -> {
						hepBuilder.hepCorrelationID(new String(msg.array(), msg.position(), (chunk_length - 6)));
						msg.position(msg.position() + chunk_length - 6);
					}
					default -> {
						log.error("Unknown default chunk: {}", chunk_type);
						msg.position(msg.position() + chunk_length - 6);
					}
				}
				i += chunk_length;
			}
		} catch (final Exception e) {
			log.error("Failed to parse HEP message", e);
			throw e;
		}
		return hepBuilder.build();
    }
	
	public static byte[] extractBytes(byte[] input, int position, int len) throws IOException, DataFormatException {
		Inflater decompress = new Inflater();
		
		decompress.setInput(input, position, len);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream(len);
		byte[] buff = new byte[1024];
		while(!decompress.finished())
		{
			int count = decompress.inflate(buff);
			baos.write(buff, 0, count);
		}
		baos.close();
		return baos.toByteArray();
	}
	
	private String readIPv4(final ByteBuffer buffer) throws Exception {
		final var address = new byte[4];
		buffer.get(address);
		return InetAddress.getByAddress(address).getHostAddress();
	}
	
	private String readIPv6(final ByteBuffer buffer) throws Exception {
		final var address = new byte[16];
		buffer.get(address);
		return Inet6Address.getByAddress(address).getHostAddress();
	}
 
}
