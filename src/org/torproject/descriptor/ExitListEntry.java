/* Copyright 2012 The Tor Project
 * See LICENSE for licensing information */
package org.torproject.descriptor;

/* Exit list entry containing results from a single exit scan. */
public interface ExitListEntry {

  /* Return the scanned relay's fingerprint. */
  public String getFingerprint();

  /* Return the publication time of the scanned relay's last known
   * descriptor. */
  public long getPublishedMillis();

  /* Return the publication time of the network status that this scan was
   * based on. */
  public long getLastStatusMillis();

  /* Return the IP address that was determined in the scan. */
  public String getExitAddress();

  /* Return the scan time. */
  public long getScanMillis();
}
