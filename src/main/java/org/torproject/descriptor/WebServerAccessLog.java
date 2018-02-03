/* Copyright 2017--2018 The Tor Project
 * See LICENSE for licensing information */

package org.torproject.descriptor;

import java.time.LocalDate;
import java.util.List;

/**
 * Contains a sanitized web server access log file from a {@code torproject.org}
 * web server.
 *
 * <p>Parsing non-sanitized web server access logs from {@code torproject.org}
 * web servers or other web servers is not explicitly supported, but may work
 * anyway.</p>
 *
 * @since 2.2.0
 */
public interface WebServerAccessLog extends LogDescriptor {

  /**
   * Returns the date when requests contained in the log have been started,
   * which is parsed from the log file path.
   *
   * <p>Typical web server access logs may contain date information in their
   * file path, too, but that would be the date when the log file was rotated,
   * which is not necessary the same date as the date in contained request
   * lines.</p>
   *
   * @since 2.2.0
   */
  public LocalDate getLogDate();

  /**
   * Returns the hostname of the physical host writing this log file, which is
   * parsed from the log file path.
   *
   * <p>A physical host can serve multiple virtual hosts, and a virtual host can
   * be served by multiple physical hosts.</p>
   *
   * @since 2.2.0
   */
  public String getPhysicalHost();

  /**
   * Returns the hostname of the virtual host that this log file was written
   * for, which is parsed from the log file path.
   *
   * <p>A physical host can serve multiple virtual hosts, and a virtual host can
   * be served by multiple physical hosts.</p>
   *
   * @since 2.2.0
   */
  public String getVirtualHost();

  /**
   * Returns at most three unrecognized lines encountered while parsing the log.
   *
   * @since 2.2.0
   */
  @Override
  public List<String> getUnrecognizedLines();

}
