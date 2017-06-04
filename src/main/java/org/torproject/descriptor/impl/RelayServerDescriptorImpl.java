/* Copyright 2015--2017 The Tor Project
 * See LICENSE for licensing information */

package org.torproject.descriptor.impl;

import org.torproject.descriptor.DescriptorParseException;
import org.torproject.descriptor.RelayServerDescriptor;

public class RelayServerDescriptorImpl extends ServerDescriptorImpl
    implements RelayServerDescriptor {

  protected RelayServerDescriptorImpl(byte[] descriptorBytes,
      int[] offsetAndLength, boolean failUnrecognizedDescriptorLines)
      throws DescriptorParseException {
    super(descriptorBytes, offsetAndLength, failUnrecognizedDescriptorLines);
    this.calculateDigestSha1Hex(Key.ROUTER.keyword + SP,
        NL + Key.ROUTER_SIGNATURE.keyword + NL);
    this.calculateDigestSha256Base64(Key.ROUTER.keyword + SP,
        NL + "-----END SIGNATURE-----" + NL);
  }
}

