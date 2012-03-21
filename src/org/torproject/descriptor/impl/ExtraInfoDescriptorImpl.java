/* Copyright 2012 The Tor Project
 * See LICENSE for licensing information */
package org.torproject.descriptor.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.torproject.descriptor.BandwidthHistory;
import org.torproject.descriptor.ExtraInfoDescriptor;

public class ExtraInfoDescriptorImpl extends DescriptorImpl
    implements ExtraInfoDescriptor {

  protected static List<ExtraInfoDescriptor> parseDescriptors(
      byte[] descriptorsBytes, boolean failUnrecognizedDescriptorLines)
      throws DescriptorParseException {
    List<ExtraInfoDescriptor> parsedDescriptors =
        new ArrayList<ExtraInfoDescriptor>();
    List<byte[]> splitDescriptorsBytes =
        DescriptorImpl.splitRawDescriptorBytes(descriptorsBytes,
        "extra-info ");
    for (byte[] descriptorBytes : splitDescriptorsBytes) {
      ExtraInfoDescriptor parsedDescriptor =
          new ExtraInfoDescriptorImpl(descriptorBytes,
              failUnrecognizedDescriptorLines);
      parsedDescriptors.add(parsedDescriptor);
    }
    return parsedDescriptors;
  }

  protected ExtraInfoDescriptorImpl(byte[] descriptorBytes,
      boolean failUnrecognizedDescriptorLines)
      throws DescriptorParseException {
    super(descriptorBytes, failUnrecognizedDescriptorLines);
    this.parseDescriptorBytes();
    Set<String> exactlyOnceKeywords = new HashSet<String>(Arrays.asList((
        "extra-info,published").split(",")));
    this.checkExactlyOnceKeywords(exactlyOnceKeywords);
    Set<String> dirreqStatsKeywords = new HashSet<String>(Arrays.asList((
        "dirreq-stats-end,dirreq-v2-ips,dirreq-v3-ips,dirreq-v2-reqs,"
        + "dirreq-v3-reqs,dirreq-v2-share,dirreq-v3-share,dirreq-v2-resp,"
        + "dirreq-v3-resp,dirreq-v2-direct-dl,dirreq-v3-direct-dl,"
        + "dirreq-v2-tunneled-dl,dirreq-v3-tunneled-dl,").split(",")));
    Set<String> entryStatsKeywords = new HashSet<String>(Arrays.asList(
        "entry-stats-end,entry-ips".split(",")));
    Set<String> cellStatsKeywords = new HashSet<String>(Arrays.asList((
        "cell-stats-end,cell-processed-cells,cell-queued-cells,"
        + "cell-time-in-queue,cell-circuits-per-decile").split(",")));
    Set<String> connBiDirectStatsKeywords = new HashSet<String>(
        Arrays.asList("conn-bi-direct".split(",")));
    Set<String> exitStatsKeywords = new HashSet<String>(Arrays.asList((
        "exit-stats-end,exit-kibibytes-written,exit-kibibytes-read,"
        + "exit-streams-opened").split(",")));
    Set<String> bridgeStatsKeywords = new HashSet<String>(Arrays.asList(
        "bridge-stats-end,bridge-stats-ips".split(",")));
    Set<String> atMostOnceKeywords = new HashSet<String>(Arrays.asList((
        "read-history,write-history,dirreq-read-history,"
        + "dirreq-write-history,geoip-db-digest,router-signature").
        split(",")));
    atMostOnceKeywords.addAll(dirreqStatsKeywords);
    atMostOnceKeywords.addAll(entryStatsKeywords);
    atMostOnceKeywords.addAll(cellStatsKeywords);
    atMostOnceKeywords.addAll(connBiDirectStatsKeywords);
    atMostOnceKeywords.addAll(exitStatsKeywords);
    atMostOnceKeywords.addAll(bridgeStatsKeywords);
    this.checkAtMostOnceKeywords(atMostOnceKeywords);
    this.checkKeywordsDependOn(dirreqStatsKeywords, "dirreq-stats-end");
    this.checkKeywordsDependOn(entryStatsKeywords, "entry-stats-end");
    this.checkKeywordsDependOn(cellStatsKeywords, "cell-stats-end");
    this.checkKeywordsDependOn(exitStatsKeywords, "exit-stats-end");
    this.checkKeywordsDependOn(bridgeStatsKeywords, "bridge-stats-end");
    this.checkFirstKeyword("extra-info");
    return;
  }

  private void parseDescriptorBytes() throws DescriptorParseException {
    try {
      BufferedReader br = new BufferedReader(new StringReader(
          new String(this.rawDescriptorBytes)));
      String line;
      boolean skipCrypto = false;
      while ((line = br.readLine()) != null) {
        String lineNoOpt = line.startsWith("opt ") ?
            line.substring("opt ".length()) : line;
        String[] partsNoOpt = lineNoOpt.split(" ");
        String keyword = partsNoOpt[0];
        if (keyword.equals("extra-info")) {
          this.parseExtraInfoLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("published")) {
          this.parsePublishedLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("read-history")) {
          this.parseReadHistoryLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("write-history")) {
          this.parseWriteHistoryLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("geoip-db-digest")) {
          this.parseGeoipDbDigestLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("geoip-start-time")) {
          this.parseGeoipStartTimeLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("geoip-client-origins")) {
          this.parseGeoipClientOriginsLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-stats-end")) {
          this.parseDirreqStatsEndLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v2-ips")) {
          this.parseDirreqV2IpsLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v3-ips")) {
          this.parseDirreqV3IpsLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v2-reqs")) {
          this.parseDirreqV2ReqsLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v3-reqs")) {
          this.parseDirreqV3ReqsLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v2-share")) {
          this.parseDirreqV2ShareLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v3-share")) {
          this.parseDirreqV3ShareLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v2-resp")) {
          this.parseDirreqV2RespLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v3-resp")) {
          this.parseDirreqV3RespLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v2-direct-dl")) {
          this.parseDirreqV2DirectDlLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v3-direct-dl")) {
          this.parseDirreqV3DirectDlLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v2-tunneled-dl")) {
          this.parseDirreqV2TunneledDlLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-v3-tunneled-dl")) {
          this.parseDirreqV3TunneledDlLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-read-history")) {
          this.parseDirreqReadHistoryLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("dirreq-write-history")) {
          this.parseDirreqWriteHistoryLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("entry-stats-end")) {
          this.parseEntryStatsEndLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("entry-ips")) {
          this.parseEntryIpsLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("cell-stats-end")) {
          this.parseCellStatsEndLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("cell-processed-cells")) {
          this.parseCellProcessedCellsLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("cell-queued-cells")) {
          this.parseCellQueuedCellsLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("cell-time-in-queue")) {
          this.parseCellTimeInQueueLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("cell-circuits-per-decile")) {
          this.parseCellCircuitsPerDecileLine(line, lineNoOpt,
              partsNoOpt);
        } else if (keyword.equals("conn-bi-direct")) {
          this.parseConnBiDirectLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("exit-stats-end")) {
          this.parseExitStatsEndLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("exit-kibibytes-written")) {
          this.parseExitKibibytesWrittenLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("exit-kibibytes-read")) {
          this.parseExitKibibytesReadLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("exit-streams-opened")) {
          this.parseExitStreamsOpenedLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("bridge-stats-end")) {
          this.parseBridgeStatsEndLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("bridge-ips")) {
          this.parseBridgeStatsIpsLine(line, lineNoOpt, partsNoOpt);
        } else if (keyword.equals("router-signature")) {
          this.parseRouterSignatureLine(line, lineNoOpt, partsNoOpt);
        } else if (line.startsWith("-----BEGIN")) {
          skipCrypto = true;
        } else if (line.startsWith("-----END")) {
          skipCrypto = false;
        } else if (!skipCrypto) {
          if (this.failUnrecognizedDescriptorLines) {
            throw new DescriptorParseException("Unrecognized line '"
                + line + "' in extra-info descriptor.");
          } else {
            if (this.unrecognizedLines == null) {
              this.unrecognizedLines = new ArrayList<String>();
            }
            this.unrecognizedLines.add(line);
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Internal error: Ran into an "
          + "IOException while parsing a String in memory.  Something's "
          + "really wrong.", e);
    }
  }

  private void parseExtraInfoLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    if (partsNoOpt.length != 3) {
      throw new DescriptorParseException("Illegal line '" + line
          + "' in extra-info descriptor.");
    }
    this.nickname = ParseHelper.parseNickname(line, partsNoOpt[1]);
    this.fingerprint = ParseHelper.parseTwentyByteHexString(line,
        partsNoOpt[2]);
  }

  private void parsePublishedLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.publishedMillis = ParseHelper.parseTimestampAtIndex(line,
        partsNoOpt, 1, 2);
  }

  private void parseReadHistoryLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.readHistory = new BandwidthHistoryImpl(line, lineNoOpt,
        partsNoOpt);
  }

  private void parseWriteHistoryLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.writeHistory = new BandwidthHistoryImpl(line, lineNoOpt,
        partsNoOpt);
  }

  private void parseGeoipDbDigestLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    if (partsNoOpt.length != 2) {
      throw new DescriptorParseException("Illegal line '" + line
          + "' in extra-info descriptor.");
    }
    this.geoipDbDigest = ParseHelper.parseTwentyByteHexString(line,
        partsNoOpt[1]);
  }

  private void parseGeoipStartTimeLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    if (partsNoOpt.length != 3) {
      throw new DescriptorParseException("Illegal line '" + line
          + "' in extra-info descriptor.");
    }
    this.geoipStartTimeMillis = ParseHelper.parseTimestampAtIndex(line,
        partsNoOpt, 1, 2);
  }

  private void parseGeoipClientOriginsLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.geoipClientOrigins = ParseHelper.parseCommaSeparatedKeyValueList(
        line, partsNoOpt, 1, 2);
  }

  private void parseDirreqStatsEndLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    long[] parsedStatsEndData = this.parseStatsEndLine(line, partsNoOpt,
        5);
    this.dirreqStatsEndMillis = parsedStatsEndData[0];
    this.dirreqStatsIntervalLength = parsedStatsEndData[1];
  }

  private long[] parseStatsEndLine(String line, String partsNoOpt[],
      int partsNoOptExpectedLength) throws DescriptorParseException {
    if (partsNoOpt.length != partsNoOptExpectedLength ||
        partsNoOpt[3].length() < 2 || !partsNoOpt[3].startsWith("(") ||
        !partsNoOpt[4].equals("s)")) {
      throw new DescriptorParseException("Illegal line '" + line + "'.");
    }
    long[] result = new long[2];
    result[0] = ParseHelper.parseTimestampAtIndex(line, partsNoOpt, 1, 2);
    result[1] = ParseHelper.parseSeconds(line,
        partsNoOpt[3].substring(1));
    return result;
  }

  private void parseDirreqV2IpsLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV2Ips = ParseHelper.parseCommaSeparatedKeyValueList(line,
        partsNoOpt, 1, 2);
  }

  private void parseDirreqV3IpsLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV3Ips = ParseHelper.parseCommaSeparatedKeyValueList(line,
        partsNoOpt, 1, 2);
  }

  private void parseDirreqV2ReqsLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV2Reqs = ParseHelper.parseCommaSeparatedKeyValueList(line,
        partsNoOpt, 1, 2);
  }

  private void parseDirreqV3ReqsLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV3Reqs = ParseHelper.parseCommaSeparatedKeyValueList(line,
        partsNoOpt, 1, 2);
  }

  private void parseDirreqV2ShareLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV2Share = this.parseShareLine(line, partsNoOpt);
  }

  private void parseDirreqV3ShareLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV3Share = this.parseShareLine(line, partsNoOpt);
  }

  private double parseShareLine(String line, String[] partsNoOpt)
      throws DescriptorParseException {
    double share = -1.0;
    if (partsNoOpt.length == 2 && partsNoOpt[1].length() >= 2 &&
        partsNoOpt[1].endsWith("%")) {
      String shareString = partsNoOpt[1];
      shareString = shareString.substring(0, shareString.length() - 1);
      try {
        share = Double.parseDouble(shareString);
      } catch (NumberFormatException e) {
        /* Handle below. */
      }
    }
    if (share < 0.0) {
      throw new DescriptorParseException("Illegal line '" + line + "'.");
    }
    return share;
  }

  private void parseDirreqV2RespLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV2Resp = ParseHelper.parseCommaSeparatedKeyValueList(line,
        partsNoOpt, 1, 0);
  }

  private void parseDirreqV3RespLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV3Resp = ParseHelper.parseCommaSeparatedKeyValueList(line,
        partsNoOpt, 1, 0);
  }

  private void parseDirreqV2DirectDlLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV2DirectDl = ParseHelper.parseCommaSeparatedKeyValueList(
        line, partsNoOpt, 1, 0);
  }

  private void parseDirreqV3DirectDlLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV3DirectDl = ParseHelper.parseCommaSeparatedKeyValueList(
        line, partsNoOpt, 1, 0);
  }

  private void parseDirreqV2TunneledDlLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV2TunneledDl = ParseHelper.parseCommaSeparatedKeyValueList(
        line, partsNoOpt, 1, 0);
  }

  private void parseDirreqV3TunneledDlLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqV3TunneledDl = ParseHelper.parseCommaSeparatedKeyValueList(
        line, partsNoOpt, 1, 0);
  }

  private void parseDirreqReadHistoryLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqReadHistory = new BandwidthHistoryImpl(line, lineNoOpt,
        partsNoOpt);
  }

  private void parseDirreqWriteHistoryLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.dirreqWriteHistory = new BandwidthHistoryImpl(line, lineNoOpt,
        partsNoOpt);
  }

  private void parseEntryStatsEndLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    long[] parsedStatsEndData = this.parseStatsEndLine(line, partsNoOpt,
        5);
    this.entryStatsEndMillis = parsedStatsEndData[0];
    this.entryStatsIntervalLength = parsedStatsEndData[1];
  }

  private void parseEntryIpsLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.entryIps = ParseHelper.parseCommaSeparatedKeyValueList(line,
        partsNoOpt, 1, 2);
  }

  private void parseCellStatsEndLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    long[] parsedStatsEndData = this.parseStatsEndLine(line, partsNoOpt,
        5);
    this.cellStatsEndMillis = parsedStatsEndData[0];
    this.cellStatsIntervalLength = parsedStatsEndData[1];
  }

  private void parseCellProcessedCellsLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.cellProcessedCells = ParseHelper.
        parseCommaSeparatedIntegerValueList(line, partsNoOpt, 1);
  }

  private void parseCellQueuedCellsLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.cellQueuedCells = ParseHelper.parseCommaSeparatedDoubleValueList(
        line, partsNoOpt, 1);
  }

  private void parseCellTimeInQueueLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.cellTimeInQueue = ParseHelper.
        parseCommaSeparatedIntegerValueList(line, partsNoOpt, 1);
  }

  private void parseCellCircuitsPerDecileLine(String line,
      String lineNoOpt, String[] partsNoOpt)
      throws DescriptorParseException {
    int circuits = -1;
    if (partsNoOpt.length == 2) {
      try {
        circuits = Integer.parseInt(partsNoOpt[1]);
      } catch (NumberFormatException e) {
        /* Handle below. */
      }
    }
    if (circuits < 0) {
      throw new DescriptorParseException("Illegal line '" + line + "'.");
    }
    this.cellCircuitsPerDecile = circuits;
  }

  private void parseConnBiDirectLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    long[] parsedStatsEndData = this.parseStatsEndLine(line, partsNoOpt,
        6);
    this.connBiDirectStatsEndMillis = parsedStatsEndData[0];
    this.connBiDirectStatsIntervalLength = parsedStatsEndData[1];
    List<Integer> parsedConnBiDirectStats = ParseHelper.
        parseCommaSeparatedIntegerValueList(line, partsNoOpt, 5);
    if (parsedConnBiDirectStats.size() != 4) {
      throw new DescriptorParseException("Illegal line '" + line + "' in "
          + "extra-info descriptor.");
    }
    this.connBiDirectBelow = parsedConnBiDirectStats.get(0);
    this.connBiDirectRead = parsedConnBiDirectStats.get(1);
    this.connBiDirectWrite = parsedConnBiDirectStats.get(2);
    this.connBiDirectBoth = parsedConnBiDirectStats.get(3);
  }

  private void parseExitStatsEndLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    long[] parsedStatsEndData = this.parseStatsEndLine(line, partsNoOpt,
        5);
    this.exitStatsEndMillis = parsedStatsEndData[0];
    this.exitStatsIntervalLength = parsedStatsEndData[1];
  }

  private void parseExitKibibytesWrittenLine(String line,
      String lineNoOpt, String[] partsNoOpt)
      throws DescriptorParseException {
    this.exitKibibytesWritten = ParseHelper.
        parseCommaSeparatedKeyValueList(line, partsNoOpt, 1, 0);
  }

  private void parseExitKibibytesReadLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.exitKibibytesRead = ParseHelper.parseCommaSeparatedKeyValueList(
        line, partsNoOpt, 1, 0);
  }

  private void parseExitStreamsOpenedLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.exitStreamsOpened = ParseHelper.parseCommaSeparatedKeyValueList(
        line, partsNoOpt, 1, 0);
  }

  private void parseBridgeStatsEndLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    long[] parsedStatsEndData = this.parseStatsEndLine(line, partsNoOpt,
        5);
    this.bridgeStatsEndMillis = parsedStatsEndData[0];
    this.bridgeStatsIntervalLength = parsedStatsEndData[1];
  }

  private void parseBridgeStatsIpsLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    this.bridgeIps = ParseHelper.parseCommaSeparatedKeyValueList(line,
        partsNoOpt, 1, 2);
  }

  private void parseRouterSignatureLine(String line, String lineNoOpt,
      String[] partsNoOpt) throws DescriptorParseException {
    if (!lineNoOpt.equals("router-signature")) {
      throw new DescriptorParseException("Illegal line '" + line + "'.");
    }
    /* Not parsing crypto parts (yet). */
  }

  private String nickname;
  public String getNickname() {
    return this.nickname;
  }

  private String fingerprint;
  public String getFingerprint() {
    return this.fingerprint;
  }

  private long publishedMillis;
  public long getPublishedMillis() {
    return this.publishedMillis;
  }

  private BandwidthHistory readHistory;
  public BandwidthHistory getReadHistory() {
    return this.readHistory;
  }

  private BandwidthHistory writeHistory;
  public BandwidthHistory getWriteHistory() {
    return this.writeHistory;
  }

  private String geoipDbDigest;
  public String getGeoipDbDigest() {
    return this.geoipDbDigest;
  }

  private long dirreqStatsEndMillis = -1L;
  public long getDirreqStatsEndMillis() {
    return this.dirreqStatsEndMillis;
  }

  private long dirreqStatsIntervalLength = -1L;
  public long getDirreqStatsIntervalLength() {
    return this.dirreqStatsIntervalLength;
  }

  private SortedMap<String, Integer> dirreqV2Ips;
  public SortedMap<String, Integer> getDirreqV2Ips() {
    return this.dirreqV2Ips == null ? null :
        new TreeMap<String, Integer>(this.dirreqV2Ips);
  }

  private SortedMap<String, Integer> dirreqV3Ips;
  public SortedMap<String, Integer> getDirreqV3Ips() {
    return this.dirreqV3Ips == null ? null :
        new TreeMap<String, Integer>(this.dirreqV3Ips);
  }

  private SortedMap<String, Integer> dirreqV2Reqs;
  public SortedMap<String, Integer> getDirreqV2Reqs() {
    return this.dirreqV2Reqs == null ? null :
        new TreeMap<String, Integer>(this.dirreqV2Reqs);
  }

  private SortedMap<String, Integer> dirreqV3Reqs;
  public SortedMap<String, Integer> getDirreqV3Reqs() {
    return this.dirreqV3Reqs == null ? null :
        new TreeMap<String, Integer>(this.dirreqV3Reqs);
  }

  private double dirreqV2Share = -1.0;
  public double getDirreqV2Share() {
    return this.dirreqV2Share;
  }

  private double dirreqV3Share = -1.0;
  public double getDirreqV3Share() {
    return this.dirreqV3Share;
  }

  private SortedMap<String, Integer> dirreqV2Resp;
  public SortedMap<String, Integer> getDirreqV2Resp() {
    return this.dirreqV2Resp == null ? null :
        new TreeMap<String, Integer>(this.dirreqV2Resp);
  }

  private SortedMap<String, Integer> dirreqV3Resp;
  public SortedMap<String, Integer> getDirreqV3Resp() {
    return this.dirreqV3Resp == null ? null :
        new TreeMap<String, Integer>(this.dirreqV3Resp);
  }

  private SortedMap<String, Integer> dirreqV2DirectDl;
  public SortedMap<String, Integer> getDirreqV2DirectDl() {
    return this.dirreqV2DirectDl == null ? null :
        new TreeMap<String, Integer>(this.dirreqV2DirectDl);
  }

  private SortedMap<String, Integer> dirreqV3DirectDl;
  public SortedMap<String, Integer> getDirreqV3DirectDl() {
    return this.dirreqV3DirectDl == null ? null :
        new TreeMap<String, Integer>(this.dirreqV3DirectDl);
  }

  private SortedMap<String, Integer> dirreqV2TunneledDl;
  public SortedMap<String, Integer> getDirreqV2TunneledDl() {
    return this.dirreqV2TunneledDl == null ? null :
        new TreeMap<String, Integer>(this.dirreqV2TunneledDl);
  }

  private SortedMap<String, Integer> dirreqV3TunneledDl;
  public SortedMap<String, Integer> getDirreqV3TunneledDl() {
    return this.dirreqV3TunneledDl == null ? null :
        new TreeMap<String, Integer>(this.dirreqV3TunneledDl);
  }

  private BandwidthHistory dirreqReadHistory;
  public BandwidthHistory getDirreqReadHistory() {
    return this.dirreqReadHistory;
  }

  private BandwidthHistory dirreqWriteHistory;
  public BandwidthHistory getDirreqWriteHistory() {
    return this.dirreqWriteHistory;
  }

  private long entryStatsEndMillis = -1L;
  public long getEntryStatsEndMillis() {
    return this.entryStatsEndMillis;
  }

  private long entryStatsIntervalLength = -1L;
  public long getEntryStatsIntervalLength() {
    return this.entryStatsIntervalLength;
  }

  private SortedMap<String, Integer> entryIps;
  public SortedMap<String, Integer> getEntryIps() {
    return this.entryIps == null ? null :
        new TreeMap<String, Integer>(this.entryIps);
  }

  private long cellStatsEndMillis = -1L;
  public long getCellStatsEndMillis() {
    return this.cellStatsEndMillis;
  }

  private long cellStatsIntervalLength = -1L;
  public long getCellStatsIntervalLength() {
    return this.cellStatsIntervalLength;
  }

  private List<Integer> cellProcessedCells;
  public List<Integer> getCellProcessedCells() {
    return this.cellProcessedCells == null ? null :
        new ArrayList<Integer>(this.cellProcessedCells);
  }

  private List<Double> cellQueuedCells;
  public List<Double> getCellQueuedCells() {
    return this.cellQueuedCells == null ? null :
        new ArrayList<Double>(this.cellQueuedCells);
  }

  private List<Integer> cellTimeInQueue;
  public List<Integer> getCellTimeInQueue() {
    return this.cellTimeInQueue == null ? null :
        new ArrayList<Integer>(this.cellTimeInQueue);
  }

  private int cellCircuitsPerDecile = -1;
  public int getCellCircuitsPerDecile() {
    return this.cellCircuitsPerDecile;
  }

  private long connBiDirectStatsEndMillis = -1L;
  public long getConnBiDirectStatsEndMillis() {
    return this.connBiDirectStatsEndMillis;
  }

  private long connBiDirectStatsIntervalLength = -1L;
  public long getConnBiDirectStatsIntervalLength() {
    return this.connBiDirectStatsIntervalLength;
  }

  private int connBiDirectBelow = -1;
  public int getConnBiDirectBelow() {
    return this.connBiDirectBelow;
  }

  private int connBiDirectRead = -1;
  public int getConnBiDirectRead() {
    return this.connBiDirectRead;
  }

  private int connBiDirectWrite = -1;
  public int getConnBiDirectWrite() {
    return this.connBiDirectWrite;
  }

  private int connBiDirectBoth = -1;
  public int getConnBiDirectBoth() {
    return this.connBiDirectBoth;
  }

  private long exitStatsEndMillis = -1L;
  public long getExitStatsEndMillis() {
    return this.exitStatsEndMillis;
  }

  private long exitStatsIntervalLength = -1L;
  public long getExitStatsIntervalLength() {
    return this.exitStatsIntervalLength;
  }

  /* TODO Add custom comparators to the maps returned by all three
   * exit-stats methods to sort keys alphanumerically, not
   * alphabetically. */

  private SortedMap<String, Integer> exitKibibytesWritten;
  public SortedMap<String, Integer> getExitKibibytesWritten() {
    return this.exitKibibytesWritten == null ? null :
        new TreeMap<String, Integer>(this.exitKibibytesWritten);
  }

  private SortedMap<String, Integer> exitKibibytesRead;
  public SortedMap<String, Integer> getExitKibibytesRead() {
    return this.exitKibibytesRead == null ? null :
        new TreeMap<String, Integer>(this.exitKibibytesRead);
  }

  private SortedMap<String, Integer> exitStreamsOpened;
  public SortedMap<String, Integer> getExitStreamsOpened() {
    return this.exitStreamsOpened == null ? null :
        new TreeMap<String, Integer>(this.exitStreamsOpened);
  }

  private long geoipStartTimeMillis = -1L;
  public long getGeoipStartTimeMillis() {
    return this.geoipStartTimeMillis;
  }

  private SortedMap<String, Integer> geoipClientOrigins;
  public SortedMap<String, Integer> getGeoipClientOrigins() {
    return this.geoipClientOrigins == null ? null :
        new TreeMap<String, Integer>(this.geoipClientOrigins);
  }

  private long bridgeStatsEndMillis = -1L;
  public long getBridgeStatsEndMillis() {
    return this.bridgeStatsEndMillis;
  }

  private long bridgeStatsIntervalLength = -1L;
  public long getBridgeStatsIntervalLength() {
    return this.bridgeStatsIntervalLength;
  }

  private SortedMap<String, Integer> bridgeIps;
  public SortedMap<String, Integer> getBridgeIps() {
    return this.bridgeIps == null ? null :
        new TreeMap<String, Integer>(this.bridgeIps);
  }
}
