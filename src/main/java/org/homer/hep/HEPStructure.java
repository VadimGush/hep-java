/*
 * $Id$
 *
 *  HEPStructure java  
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

package org.homer.hep;

import lombok.Builder;
import lombok.Value;

import java.nio.ByteBuffer;

/**
 * See documentation for the HEP protocol and meaning for all of those fields:
 * <a href="https://github.com/sipcapture/HEP/blob/master/docs/HEP3_Network_Protocol_Specification_REV_36.pdf">HEP documentation</a>
 */
@Builder
@Value
public class HEPStructure {
	
	// IP protocol family
	@Builder.Default
	int ipFamily  = 0;
	// IP protocol ID (tcp, udp, etc.)
	@Builder.Default
	int protocolId = 0;
	@Builder.Default
	int sourcePort = 0;
	@Builder.Default
	int destinationPort = 0;
	@Builder.Default
	long timeSeconds = 0;
	@Builder.Default
	long timeUseconds = 0;
	// Protocol type (SIP/H323/RTP/MGCP etc.)
	@Builder.Default
	int protocolType = 0;
	@Builder.Default
	int captureId = 0;
	@Builder.Default
	String hepCorrelationID = null;
	@Builder.Default
	String captureAuthUser = null;
	@Builder.Default
	String sourceIPAddress = null;
	@Builder.Default
	String destinationIPAddress = null;
	@Builder.Default
	ByteBuffer payloadByteMessage = null;

	@Override
    public String toString(){
		return "HEPStructure[" +
			"ipFamily: " + ipFamily +
			", protcolId: " + protocolId +
			", sourcePort: " + sourcePort +
			", destinationPort: " + destinationPort +
			", timeSeconds: " + timeSeconds +
			", timeUseconds: " + timeUseconds +
			", protocolType: " + protocolType +
			", captureId: " + captureId +
			", hepCorrelationID: " + hepCorrelationID +
			", captureAuthUser: " + captureAuthUser +
			", sourceIPAddress: " + sourceIPAddress +
			", destinationIPAddress: " + destinationIPAddress +
			", payloadByteMessage: " + payloadByteMessage +
			"]";
    }
}
