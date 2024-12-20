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

@Builder
@Value
public class HEPStructure {
	
	@Builder.Default
	int ipFamily  = 0;
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
	    StringBuilder sb = new StringBuilder();
	    sb.append("HEPStructure[")
                .append("ipFamily: " + ipFamily)
                .append(", protcolId: " + protocolId)
                .append(", sourcePort: " + sourcePort)
                .append(", destinationPort: " + destinationPort)
                .append(", timeSeconds: " + timeSeconds)
                .append(", timeUseconds: " + timeUseconds)
                .append(", protocolType: " + protocolType)
                .append(", captureId: " + captureId)
                .append(", hepCorrelationID: " + hepCorrelationID)
                .append(", captureAuthUser: " + captureAuthUser)
                .append(", sourceIPAddress: " + sourceIPAddress)
                .append(", destinationIPAddress: " + destinationIPAddress)
                .append(", payloadByteMessage: " + payloadByteMessage)
                .append("]");
	    return sb.toString();
    }
}
