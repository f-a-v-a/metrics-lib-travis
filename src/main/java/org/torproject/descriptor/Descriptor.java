/* Copyright 2011--2017 The Tor Project
 * See LICENSE for licensing information */

package org.torproject.descriptor;

import java.io.File;
import java.util.List;

/**
 * Superinterface for any descriptor with access to generic information
 * about the descriptor.
 *
 * @since 1.0.0
 */
public interface Descriptor {

  /**
   * Return the raw descriptor bytes.
   *
   * @since 1.0.0
   */
  public byte[] getRawDescriptorBytes();

  /**
   * Return the (possibly empty) list of annotations in the format
   * {@code "@key( value)*"}.
   *
   * <p>Some implementations might not support this operation and will throw an
   * {@code UnsupportedOperationException}, e.g.,
   * {@link UnparseableDescriptor}.</p>
   *
   * @since 1.0.0
   */
  public List<String> getAnnotations();

  /**
   * Return any unrecognized lines when parsing this descriptor, or an
   * empty list if there were no unrecognized lines.
   *
   * <p>Some implementations might not support this operation and will throw an
   * {@code UnsupportedOperationException}, * e.g.,
   * {@link UnparseableDescriptor}.</p>
   *
   * @since 1.0.0
   */
  public List<String> getUnrecognizedLines();

  /**
   * Return the file, tarball or plain file, that contained this descriptor, or
   * {@code null} if this descriptor was not read from a file.
   *
   * @return Descriptor file that contained this descriptor.
   *
   * @since 1.9.0
   */
  public File getDescriptorFile();
}

