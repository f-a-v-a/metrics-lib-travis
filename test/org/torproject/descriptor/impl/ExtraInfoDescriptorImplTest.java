/* Copyright 2012 The Tor Project
 * See LICENSE for licensing information */
package org.torproject.descriptor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.torproject.descriptor.BandwidthHistory;
import org.torproject.descriptor.ExtraInfoDescriptor;

/* Test parsing of extra-info descriptors. */
public class ExtraInfoDescriptorImplTest {

  /* Helper class to build a descriptor based on default data and
   * modifications requested by test methods. */
  private static class DescriptorBuilder {
    private String extraInfoLine = "extra-info chaoscomputerclub5 "
        + "A9C039A5FD02FCA06303DCFAABE25C5912C63B26";
    private static ExtraInfoDescriptor createWithExtraInfoLine(
        String line) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.extraInfoLine = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String publishedLine = "published 2012-02-11 09:08:36";
    private static ExtraInfoDescriptor createWithPublishedLine(
        String line) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.publishedLine = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String writeHistoryLine = "write-history 2012-02-11 09:03:39 "
        + "(900 s) 4713350144,4723824640,4710717440,4572675072";
    private static ExtraInfoDescriptor createWithWriteHistoryLine(
        String line) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.writeHistoryLine = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String readHistoryLine = "read-history 2012-02-11 09:03:39 "
        + "(900 s) 4707695616,4699666432,4650004480,4489718784";
    private static ExtraInfoDescriptor createWithReadHistoryLine(
        String line) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.readHistoryLine = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String dirreqWriteHistoryLine = "dirreq-write-history "
        + "2012-02-11 09:03:39 (900 s) 81281024,64996352,60625920,"
        + "67922944";
    private static ExtraInfoDescriptor createWithDirreqWriteHistoryLine(
        String line) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.dirreqWriteHistoryLine = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String dirreqReadHistoryLine = "dirreq-read-history "
        + "2012-02-11 09:03:39 (900 s) 17074176,16235520,16005120,"
        + "16209920";
    private static ExtraInfoDescriptor createWithDirreqReadHistoryLine(
        String line) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.dirreqReadHistoryLine = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String geoipDbDigestLine = null;
    private static ExtraInfoDescriptor createWithGeoipDbDigestLine(
        String line) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.geoipDbDigestLine = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String geoipStatsLines = null;
    private static ExtraInfoDescriptor createWithGeoipStatsLines(
        String lines) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.geoipStatsLines = lines;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String dirreqStatsLines = null;
    private static ExtraInfoDescriptor createWithDirreqStatsLines(
        String lines) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.dirreqStatsLines = lines;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String entryStatsLines = null;
    private static ExtraInfoDescriptor createWithEntryStatsLines(
        String lines) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.entryStatsLines = lines;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String cellStatsLines = null;
    private static ExtraInfoDescriptor createWithCellStatsLines(
        String lines) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.cellStatsLines = lines;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String connBiDirectLine = null;
    private static ExtraInfoDescriptor createWithConnBiDirectLine(
        String line) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.connBiDirectLine = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String exitStatsLines = null;
    private static ExtraInfoDescriptor createWithExitStatsLines(
        String lines) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.exitStatsLines = lines;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String bridgeStatsLines = null;
    private static ExtraInfoDescriptor createWithBridgeStatsLines(
        String lines) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.bridgeStatsLines = lines;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private String unrecognizedLine = null;
    private static ExtraInfoDescriptor createWithUnrecognizedLine(
        String line, boolean failUnrecognizedDescriptorLines)
        throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.unrecognizedLine = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(),
          failUnrecognizedDescriptorLines);
    }
    private String routerSignatureLines = "router-signature\n"
        + "-----BEGIN SIGNATURE-----\n"
        + "o4j+kH8UQfjBwepUnr99v0ebN8RpzHJ/lqYsTojXHy9kMr1RNI9IDeSzA7PSqT"
        + "uV\n4PL8QsGtlfwthtIoZpB2srZeyN/mcpA9fa1JXUrt/UN9K/+32Cyaad7h0n"
        + "HE6Xfb\njqpXDpnBpvk4zjmzjjKYnIsUWTnADmu0fo3xTRqXi7g=\n"
        + "-----END SIGNATURE-----";
    private static ExtraInfoDescriptor createWithRouterSignatureLines(
        String line) throws DescriptorParseException {
      DescriptorBuilder db = new DescriptorBuilder();
      db.routerSignatureLines = line;
      return new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    }
    private byte[] buildDescriptor() {
      StringBuilder sb = new StringBuilder();
      if (this.extraInfoLine != null) {
        sb.append(this.extraInfoLine + "\n");
      }
      if (this.publishedLine != null) {
        sb.append(this.publishedLine + "\n");
      }
      if (this.writeHistoryLine != null) {
        sb.append(this.writeHistoryLine + "\n");
      }
      if (this.readHistoryLine != null) {
        sb.append(this.readHistoryLine + "\n");
      }
      if (this.dirreqWriteHistoryLine != null) {
        sb.append(this.dirreqWriteHistoryLine + "\n");
      }
      if (this.dirreqReadHistoryLine != null) {
        sb.append(this.dirreqReadHistoryLine + "\n");
      }
      if (this.geoipDbDigestLine != null) {
        sb.append(this.geoipDbDigestLine + "\n");
      }
      if (this.geoipStatsLines != null) {
        sb.append(this.geoipStatsLines + "\n");
      }
      if (this.dirreqStatsLines != null) {
        sb.append(this.dirreqStatsLines + "\n");
      }
      if (this.entryStatsLines != null) {
        sb.append(this.entryStatsLines + "\n");
      }
      if (this.cellStatsLines != null) {
        sb.append(this.cellStatsLines + "\n");
      }
      if (this.connBiDirectLine != null) {
        sb.append(this.connBiDirectLine + "\n");
      }
      if (this.exitStatsLines != null) {
        sb.append(this.exitStatsLines + "\n");
      }
      if (this.bridgeStatsLines != null) {
        sb.append(this.bridgeStatsLines + "\n");
      }
      if (this.unrecognizedLine != null) {
        sb.append(this.unrecognizedLine + "\n");
      }
      if (this.routerSignatureLines != null) {
        sb.append(this.routerSignatureLines + "\n");
      }
      return sb.toString().getBytes();
    }
  }

  /* Helper class to build a set of geoip-stats lines based on default
   * data and modifications requested by test methods. */
  private static class GeoipStatsBuilder {
    private String geoipStartTimeLine = "geoip-start-time 2012-02-10 "
        + "18:32:51";
    private static ExtraInfoDescriptor createWithGeoipStartTimeLine(
        String line) throws DescriptorParseException {
      GeoipStatsBuilder gsb = new GeoipStatsBuilder();
      gsb.geoipStartTimeLine = line;
      return DescriptorBuilder.createWithGeoipStatsLines(
          gsb.buildGeoipStatsLines());
    }
    private String geoipClientOriginsLine = "geoip-client-origins "
        + "de=1152,cn=896,us=712,it=504,ru=352,fr=208,gb=208,ir=200";
    private static ExtraInfoDescriptor createWithGeoipClientOriginsLine(
        String line) throws DescriptorParseException {
      GeoipStatsBuilder gsb = new GeoipStatsBuilder();
      gsb.geoipClientOriginsLine = line;
      return DescriptorBuilder.createWithGeoipStatsLines(
          gsb.buildGeoipStatsLines());
    }
    private static ExtraInfoDescriptor createWithDefaultLines()
        throws DescriptorParseException {
      return DescriptorBuilder.createWithGeoipStatsLines(
          new GeoipStatsBuilder().buildGeoipStatsLines());
    }
    private String buildGeoipStatsLines() {
      StringBuilder sb = new StringBuilder();
      if (this.geoipStartTimeLine != null) {
        sb.append(this.geoipStartTimeLine + "\n");
      }
      if (this.geoipClientOriginsLine != null) {
        sb.append(this.geoipClientOriginsLine + "\n");
      }
      String lines = sb.toString();
      if (lines.endsWith("\n")) {
        lines = lines.substring(0, lines.length() - 1);
      }
      return lines;
    }
  }

  /* Helper class to build a set of dirreq-stats lines based on default
   * data and modifications requested by test methods. */
  private static class DirreqStatsBuilder {
    private String dirreqStatsEndLine = "dirreq-stats-end 2012-02-11 "
        + "00:59:53 (86400 s)";
    private static ExtraInfoDescriptor createWithDirreqStatsEndLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqStatsEndLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV3IpsLine = "dirreq-v3-ips us=1544,de=1056,"
        + "it=1032,fr=784,es=640,ru=440,br=312,gb=272,kr=224,sy=192";
    private static ExtraInfoDescriptor createWithDirreqV3IpsLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV3IpsLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV2IpsLine = "dirreq-v2-ips ";
    private static ExtraInfoDescriptor createWithDirreqV2IpsLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV2IpsLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV3ReqsLine = "dirreq-v3-reqs us=1744,de=1224,"
        + "it=1080,fr=832,es=664,ru=536,br=344,gb=296,kr=272,in=216";
    private static ExtraInfoDescriptor createWithDirreqV3ReqsLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV3ReqsLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV2ReqsLine = "dirreq-v2-reqs ";
    private static ExtraInfoDescriptor createWithDirreqV2ReqsLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV2ReqsLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV3RespLine = "dirreq-v3-resp ok=10848,"
        + "not-enough-sigs=8,unavailable=0,not-found=0,not-modified=0,"
        + "busy=80";
    private static ExtraInfoDescriptor createWithDirreqV3RespLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV3RespLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV2RespLine = "dirreq-v2-resp ok=0,unavailable=0,"
        + "not-found=1576,not-modified=0,busy=0";
    private static ExtraInfoDescriptor createWithDirreqV2RespLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV2RespLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV2ShareLine = "dirreq-v2-share 0.37%";
    private static ExtraInfoDescriptor createWithDirreqV2ShareLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV2ShareLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV3ShareLine = "dirreq-v3-share 0.37%";
    private static ExtraInfoDescriptor createWithDirreqV3ShareLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV3ShareLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV3DirectDlLine = "dirreq-v3-direct-dl "
        + "complete=36,timeout=4,running=0,min=7538,d1=20224,d2=28950,"
        + "q1=40969,d3=55786,d4=145813,md=199164,d6=267230,d7=480900,"
        + "q3=481049,d8=531276,d9=778086,max=15079428";
    private static ExtraInfoDescriptor createWithDirreqV3DirectDlLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV3DirectDlLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV2DirectDlLine = "dirreq-v2-direct-dl "
        + "complete=0,timeout=0,running=0";
    private static ExtraInfoDescriptor createWithDirreqV2DirectDlLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV2DirectDlLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV3TunneledDlLine = "dirreq-v3-tunneled-dl "
        + "complete=10608,timeout=204,running=4,min=507,d1=20399,"
        + "d2=27588,q1=29292,d3=30889,d4=40624,md=59967,d6=103333,"
        + "d7=161170,q3=209415,d8=256711,d9=452503,max=23417777";
    private static ExtraInfoDescriptor createWithDirreqV3TunneledDlLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV3TunneledDlLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private String dirreqV2TunneledDlLine = "dirreq-v2-tunneled-dl "
        + "complete=0,timeout=0,running=0";
    private static ExtraInfoDescriptor createWithDirreqV2TunneledDlLine(
        String line) throws DescriptorParseException {
      DirreqStatsBuilder dsb = new DirreqStatsBuilder();
      dsb.dirreqV2TunneledDlLine = line;
      return DescriptorBuilder.createWithDirreqStatsLines(
          dsb.buildDirreqStatsLines());
    }
    private static ExtraInfoDescriptor createWithDefaultLines()
        throws DescriptorParseException {
      return DescriptorBuilder.createWithDirreqStatsLines(
          new DirreqStatsBuilder().buildDirreqStatsLines());
    }
    private String buildDirreqStatsLines() {
      StringBuilder sb = new StringBuilder();
      if (this.dirreqStatsEndLine != null) {
        sb.append(this.dirreqStatsEndLine + "\n");
      }
      if (this.dirreqV3IpsLine != null) {
        sb.append(this.dirreqV3IpsLine + "\n");
      }
      if (this.dirreqV2IpsLine != null) {
        sb.append(this.dirreqV2IpsLine + "\n");
      }
      if (this.dirreqV3ReqsLine != null) {
        sb.append(this.dirreqV3ReqsLine + "\n");
      }
      if (this.dirreqV2ReqsLine != null) {
        sb.append(this.dirreqV2ReqsLine + "\n");
      }
      if (this.dirreqV3RespLine != null) {
        sb.append(this.dirreqV3RespLine + "\n");
      }
      if (this.dirreqV2RespLine != null) {
        sb.append(this.dirreqV2RespLine + "\n");
      }
      if (this.dirreqV2ShareLine != null) {
        sb.append(this.dirreqV2ShareLine + "\n");
      }
      if (this.dirreqV3ShareLine != null) {
        sb.append(this.dirreqV3ShareLine + "\n");
      }
      if (this.dirreqV3DirectDlLine != null) {
        sb.append(this.dirreqV3DirectDlLine + "\n");
      }
      if (this.dirreqV2DirectDlLine != null) {
        sb.append(this.dirreqV2DirectDlLine + "\n");
      }
      if (this.dirreqV3TunneledDlLine != null) {
        sb.append(this.dirreqV3TunneledDlLine + "\n");
      }
      if (this.dirreqV2TunneledDlLine != null) {
        sb.append(this.dirreqV2TunneledDlLine + "\n");
      }
      String lines = sb.toString();
      if (lines.endsWith("\n")) {
        lines = lines.substring(0, lines.length() - 1);
      }
      return lines;
    }
  }

  /* Helper class to build a set of entry-stats lines based on default
   * data and modifications requested by test methods. */
  private static class EntryStatsBuilder {
    private String entryStatsEndLine = "entry-stats-end 2012-02-11 "
        + "01:59:39 (86400 s)";
    private static ExtraInfoDescriptor createWithEntryStatsEndLine(
        String line) throws DescriptorParseException {
      EntryStatsBuilder esb = new EntryStatsBuilder();
      esb.entryStatsEndLine = line;
      return DescriptorBuilder.createWithEntryStatsLines(
          esb.buildEntryStatsLines());
    }
    private String entryIpsLine = "entry-ips ir=25368,us=15744,it=14816,"
        + "de=13256,es=8280,fr=8120,br=5176,sy=4760,ru=4504,sa=4216,"
        + "gb=3152,pl=2928,nl=2208,kr=1856,ca=1792,ua=1272,in=1192";
    private static ExtraInfoDescriptor createWithEntryIpsLine(
        String line) throws DescriptorParseException {
      EntryStatsBuilder esb = new EntryStatsBuilder();
      esb.entryIpsLine = line;
      return DescriptorBuilder.createWithEntryStatsLines(
          esb.buildEntryStatsLines());
    }
    private static ExtraInfoDescriptor createWithDefaultLines()
        throws DescriptorParseException {
      return DescriptorBuilder.createWithEntryStatsLines(
          new EntryStatsBuilder().buildEntryStatsLines());
    }
    private String buildEntryStatsLines() {
      StringBuilder sb = new StringBuilder();
      if (this.entryStatsEndLine != null) {
        sb.append(this.entryStatsEndLine + "\n");
      }
      if (this.entryIpsLine != null) {
        sb.append(this.entryIpsLine + "\n");
      }
      String lines = sb.toString();
      if (lines.endsWith("\n")) {
        lines = lines.substring(0, lines.length() - 1);
      }
      return lines;
    }
  }

  /* Helper class to build a set of cell-stats lines based on default
   * data and modifications requested by test methods. */
  private static class CellStatsBuilder {
    private String cellStatsEndLine = "cell-stats-end 2012-02-11 "
        + "01:59:39 (86400 s)";
    private static ExtraInfoDescriptor createWithCellStatsEndLine(
        String line) throws DescriptorParseException {
      CellStatsBuilder csb = new CellStatsBuilder();
      csb.cellStatsEndLine = line;
      return DescriptorBuilder.createWithCellStatsLines(
          csb.buildCellStatsLines());
    }
    private String cellProcessedCellsLine = "cell-processed-cells "
        + "1441,11,6,4,2,1,1,1,1,1";
    private static ExtraInfoDescriptor createWithCellProcessedCellsLine(
        String line) throws DescriptorParseException {
      CellStatsBuilder csb = new CellStatsBuilder();
      csb.cellProcessedCellsLine = line;
      return DescriptorBuilder.createWithCellStatsLines(
          csb.buildCellStatsLines());
    }
    private String cellQueuedCellsLine = "cell-queued-cells "
        + "3.29,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00";
    private static ExtraInfoDescriptor createWithCellQueuedCellsLine(
        String line) throws DescriptorParseException {
      CellStatsBuilder csb = new CellStatsBuilder();
      csb.cellQueuedCellsLine = line;
      return DescriptorBuilder.createWithCellStatsLines(
          csb.buildCellStatsLines());
    }
    private String cellTimeInQueueLine = "cell-time-in-queue "
        + "524,1,1,0,0,25,0,0,0,0";
    private static ExtraInfoDescriptor createWithCellTimeInQueueLine(
        String line) throws DescriptorParseException {
      CellStatsBuilder csb = new CellStatsBuilder();
      csb.cellTimeInQueueLine = line;
      return DescriptorBuilder.createWithCellStatsLines(
          csb.buildCellStatsLines());
    }
    private String cellCircuitsPerDecileLine = "cell-circuits-per-decile "
        + "866";
    private static ExtraInfoDescriptor
        createWithCellCircuitsPerDecileLine(String line)
        throws DescriptorParseException {
      CellStatsBuilder csb = new CellStatsBuilder();
      csb.cellCircuitsPerDecileLine = line;
      return DescriptorBuilder.createWithCellStatsLines(
          csb.buildCellStatsLines());
    }
    private static ExtraInfoDescriptor createWithDefaultLines()
        throws DescriptorParseException {
      return DescriptorBuilder.createWithCellStatsLines(
          new CellStatsBuilder().buildCellStatsLines());
    }
    private String buildCellStatsLines() {
      StringBuilder sb = new StringBuilder();
      if (this.cellStatsEndLine != null) {
        sb.append(this.cellStatsEndLine + "\n");
      }
      if (this.cellProcessedCellsLine != null) {
        sb.append(this.cellProcessedCellsLine + "\n");
      }
      if (this.cellQueuedCellsLine != null) {
        sb.append(this.cellQueuedCellsLine + "\n");
      }
      if (this.cellTimeInQueueLine != null) {
        sb.append(this.cellTimeInQueueLine + "\n");
      }
      if (this.cellCircuitsPerDecileLine != null) {
        sb.append(this.cellCircuitsPerDecileLine + "\n");
      }
      String lines = sb.toString();
      if (lines.endsWith("\n")) {
        lines = lines.substring(0, lines.length() - 1);
      }
      return lines;
    }
  }

  /* Helper class to build a set of exit-stats lines based on default
   * data and modifications requested by test methods. */
  private static class ExitStatsBuilder {
    private String exitStatsEndLine = "exit-stats-end 2012-02-11 "
        + "01:59:39 (86400 s)";
    private static ExtraInfoDescriptor createWithExitStatsEndLine(
        String line) throws DescriptorParseException {
      ExitStatsBuilder esb = new ExitStatsBuilder();
      esb.exitStatsEndLine = line;
      return DescriptorBuilder.createWithExitStatsLines(
          esb.buildExitStatsLines());
    }
    private String exitKibibytesWrittenLine = "exit-kibibytes-written "
        + "25=74647,80=31370,443=20577,49755=23,52563=12,52596=1111,"
        + "57528=4,60912=11,61351=6,64811=3365,other=2592";
    private static ExtraInfoDescriptor createWithExitKibibytesWrittenLine(
        String line) throws DescriptorParseException {
      ExitStatsBuilder esb = new ExitStatsBuilder();
      esb.exitKibibytesWrittenLine = line;
      return DescriptorBuilder.createWithExitStatsLines(
          esb.buildExitStatsLines());
    }
    private String exitKibibytesReadLine = "exit-kibibytes-read "
        + "25=35562,80=1254256,443=110279,49755=9396,52563=1911,"
        + "52596=648,57528=1188,60912=1427,61351=1824,64811=14,"
        + "other=3054";
    private static ExtraInfoDescriptor createWithExitKibibytesReadLine(
        String line) throws DescriptorParseException {
      ExitStatsBuilder esb = new ExitStatsBuilder();
      esb.exitKibibytesReadLine = line;
      return DescriptorBuilder.createWithExitStatsLines(
          esb.buildExitStatsLines());
    }
    private String exitStreamsOpenedLine = "exit-streams-opened "
        + "25=369748,80=64212,443=151660,49755=4,52563=4,52596=4,57528=4,"
        + "60912=4,61351=4,64811=4,other=1212";
    private static ExtraInfoDescriptor createWithExitStreamsOpenedLine(
        String line) throws DescriptorParseException {
      ExitStatsBuilder esb = new ExitStatsBuilder();
      esb.exitStreamsOpenedLine = line;
      return DescriptorBuilder.createWithExitStatsLines(
          esb.buildExitStatsLines());
    }
    private static ExtraInfoDescriptor createWithDefaultLines()
        throws DescriptorParseException {
      return DescriptorBuilder.createWithExitStatsLines(
          new ExitStatsBuilder().buildExitStatsLines());
    }
    private String buildExitStatsLines() {
      StringBuilder sb = new StringBuilder();
      if (this.exitStatsEndLine != null) {
        sb.append(this.exitStatsEndLine + "\n");
      }
      if (this.exitKibibytesWrittenLine != null) {
        sb.append(this.exitKibibytesWrittenLine + "\n");
      }
      if (this.exitKibibytesReadLine != null) {
        sb.append(this.exitKibibytesReadLine + "\n");
      }
      if (this.exitStreamsOpenedLine != null) {
        sb.append(this.exitStreamsOpenedLine + "\n");
      }
      String lines = sb.toString();
      if (lines.endsWith("\n")) {
        lines = lines.substring(0, lines.length() - 1);
      }
      return lines;
    }
  }

  /* Helper class to build a set of bridge-stats lines based on default
   * data and modifications requested by test methods. */
  private static class BridgeStatsBuilder {
    private String bridgeStatsEndLine = "bridge-stats-end 2012-02-11 "
        + "01:59:39 (86400 s)";
    private static ExtraInfoDescriptor createWithBridgeStatsEndLine(
        String line) throws DescriptorParseException {
      BridgeStatsBuilder bsb = new BridgeStatsBuilder();
      bsb.bridgeStatsEndLine = line;
      return DescriptorBuilder.createWithBridgeStatsLines(
          bsb.buildBridgeStatsLines());
    }
    private String bridgeIpsLine = "bridge-ips ir=24,sy=16,??=8,cn=8,"
        + "de=8,es=8,fr=8,gb=8,in=8,jp=8,kz=8,nl=8,ua=8,us=8,vn=8,za=8";
    private static ExtraInfoDescriptor createWithBridgeIpsLine(
        String line) throws DescriptorParseException {
      BridgeStatsBuilder bsb = new BridgeStatsBuilder();
      bsb.bridgeIpsLine = line;
      return DescriptorBuilder.createWithBridgeStatsLines(
          bsb.buildBridgeStatsLines());
    }
    private static ExtraInfoDescriptor createWithDefaultLines()
        throws DescriptorParseException {
      return DescriptorBuilder.createWithBridgeStatsLines(
          new BridgeStatsBuilder().buildBridgeStatsLines());
    }
    private String buildBridgeStatsLines() {
      StringBuilder sb = new StringBuilder();
      if (this.bridgeStatsEndLine != null) {
        sb.append(this.bridgeStatsEndLine + "\n");
      }
      if (this.bridgeIpsLine != null) {
        sb.append(this.bridgeIpsLine + "\n");
      }
      String lines = sb.toString();
      if (lines.endsWith("\n")) {
        lines = lines.substring(0, lines.length() - 1);
      }
      return lines;
    }
  }

  @Test()
  public void testSampleDescriptor() throws DescriptorParseException {
    DescriptorBuilder db = new DescriptorBuilder();
    ExtraInfoDescriptor descriptor =
        new ExtraInfoDescriptorImpl(db.buildDescriptor(), true);
    assertEquals("chaoscomputerclub5", descriptor.getNickname());
    assertEquals("A9C039A5FD02FCA06303DCFAABE25C5912C63B26",
        descriptor.getFingerprint());
    assertEquals(1328951316000L, descriptor.getPublishedMillis());
    assertNotNull(descriptor.getWriteHistory());
    assertEquals(1328951019000L, descriptor.getWriteHistory().
        getHistoryEndMillis());
    assertEquals(900L, descriptor.getWriteHistory().getIntervalLength());
    assertEquals(4572675072L, (long) descriptor.getWriteHistory().
        getBandwidthValues().get(1328951019000L));
    assertNotNull(descriptor.getReadHistory());
    assertNotNull(descriptor.getDirreqWriteHistory());
    assertNotNull(descriptor.getDirreqReadHistory());
  }

  @Test(expected = DescriptorParseException.class)
  public void testExtraInfoLineMissing() throws DescriptorParseException {
    DescriptorBuilder.createWithExtraInfoLine(null);
  }

  @Test()
  public void testExtraInfoOpt() throws DescriptorParseException {
    ExtraInfoDescriptor descriptor = DescriptorBuilder.
        createWithExtraInfoLine("opt extra-info chaoscomputerclub5 "
        + "A9C039A5FD02FCA06303DCFAABE25C5912C63B26");
    assertEquals("chaoscomputerclub5", descriptor.getNickname());
    assertEquals("A9C039A5FD02FCA06303DCFAABE25C5912C63B26",
        descriptor.getFingerprint());
  }

  @Test(expected = DescriptorParseException.class)
  public void testExtraInfoLineNotFirst()
      throws DescriptorParseException {
    DescriptorBuilder.createWithExtraInfoLine("geoip-db-digest "
        + "916A3CA8B7DF61473D5AE5B21711F35F301CE9E8\n"
        + "extra-info chaoscomputerclub5 "
        + "A9C039A5FD02FCA06303DCFAABE25C5912C63B26");
  }

  @Test(expected = DescriptorParseException.class)
  public void testNicknameMissing() throws DescriptorParseException {
    DescriptorBuilder.createWithExtraInfoLine("extra-info  "
        + "A9C039A5FD02FCA06303DCFAABE25C5912C63B26");
  }

  @Test(expected = DescriptorParseException.class)
  public void testNicknameInvalidChar() throws DescriptorParseException {
    DescriptorBuilder.createWithExtraInfoLine("extra-info "
        + "chaoscomputerclub% A9C039A5FD02FCA06303DCFAABE25C5912C63B26");
  }

  @Test(expected = DescriptorParseException.class)
  public void testNicknameTooLong() throws DescriptorParseException {
    DescriptorBuilder.createWithExtraInfoLine("extra-info "
        + "chaoscomputerclub5ReallyLongNickname "
        + "A9C039A5FD02FCA06303DCFAABE25C5912C63B26");
  }

  @Test(expected = DescriptorParseException.class)
  public void testFingerprintG() throws DescriptorParseException {
    DescriptorBuilder.createWithExtraInfoLine("extra-info "
        + "chaoscomputerclub5 G9C039A5FD02FCA06303DCFAABE25C5912C63B26");
  }

  @Test(expected = DescriptorParseException.class)
  public void testFingerprintTooShort() throws DescriptorParseException {
    DescriptorBuilder.createWithExtraInfoLine("extra-info "
        + "chaoscomputerclub5 A9C039A5FD02FCA06303DCFAABE25C5912C6");
  }

  @Test(expected = DescriptorParseException.class)
  public void testFingerprintTooLong() throws DescriptorParseException {
    DescriptorBuilder.createWithExtraInfoLine("extra-info "
        + "chaoscomputerclub5 A9C039A5FD02FCA06303DCFAABE25C5912C63B26"
        + "A9C0");
  }

  @Test(expected = DescriptorParseException.class)
  public void testPublishedMissing() throws DescriptorParseException {
    DescriptorBuilder.createWithPublishedLine(null);
  }

  @Test()
  public void testPublishedOpt() throws DescriptorParseException {
    ExtraInfoDescriptor descriptor = DescriptorBuilder.
        createWithPublishedLine("opt published 2012-02-11 09:08:36");
    assertEquals(1328951316000L, descriptor.getPublishedMillis());
  }

  @Test()
  public void testGeoipDbDigestValid() throws DescriptorParseException {
    ExtraInfoDescriptor descriptor = DescriptorBuilder.
        createWithGeoipDbDigestLine("geoip-db-digest "
        + "916A3CA8B7DF61473D5AE5B21711F35F301CE9E8");
    assertEquals("916A3CA8B7DF61473D5AE5B21711F35F301CE9E8",
        descriptor.getGeoipDbDigest());
  }

  /* TODO Add tests for invalid geoip-db-digest lines. */

  @Test()
  public void testGeoipStatsValid() throws DescriptorParseException {
    GeoipStatsBuilder.createWithDefaultLines();
    /* TODO Check stats parts. */
  }

  /* TODO Add tests for invalid geoip stats. */

  @Test()
  public void testDirreqStatsValid() throws DescriptorParseException {
    DirreqStatsBuilder.createWithDefaultLines();
    /* TODO Check stats parts. */
  }

  /* TODO Add tests for invalid dirreq stats. */

  @Test()
  public void testEntryStatsValid() throws DescriptorParseException {
    EntryStatsBuilder.createWithDefaultLines();
    /* TODO Check stats parts. */
  }

  /* TODO Add tests for invalid entry stats. */

  @Test()
  public void testCellStatsValid() throws DescriptorParseException {
    CellStatsBuilder.createWithDefaultLines();
    /* TODO Check stats parts. */
  }

  /* TODO Add tests for invalid cell stats. */

  @Test()
  public void testConnBiDirectValid()
      throws DescriptorParseException {
    DescriptorBuilder.createWithConnBiDirectLine("conn-bi-direct "
        + "2012-02-11 01:59:39 (86400 s) 42173,1591,1310,1744");
    /* TODO Check stats parts. */
  }

  /* TODO Add tests for invalid conn-bi-direct stats. */

  @Test()
  public void testExitStatsValid() throws DescriptorParseException {
    ExitStatsBuilder.createWithDefaultLines();
    /* TODO Check stats parts. */
  }

  /* TODO Add tests for invalid exit stats. */

  @Test()
  public void testBridgeStatsValid() throws DescriptorParseException {
    BridgeStatsBuilder.createWithDefaultLines();
    /* TODO Check stats parts. */
  }

  /* TODO Add tests for invalid bridge stats. */

  @Test()
  public void testRouterSignatureOpt()
      throws DescriptorParseException {
    DescriptorBuilder.createWithRouterSignatureLines("opt "
        + "router-signature\n"
        + "-----BEGIN SIGNATURE-----\n"
        + "crypto lines are ignored anyway\n"
        + "-----END SIGNATURE-----");
  }

  @Test(expected = DescriptorParseException.class)
  public void testRouterSignatureNotLastLine()
      throws DescriptorParseException {
    DescriptorBuilder.createWithRouterSignatureLines("router-signature\n"
        + "-----BEGIN SIGNATURE-----\n"
        + "o4j+kH8UQfjBwepUnr99v0ebN8RpzHJ/lqYsTojXHy9kMr1RNI9IDeSzA7PSqT"
        + "uV\n4PL8QsGtlfwthtIoZpB2srZeyN/mcpA9fa1JXUrt/UN9K/+32Cyaad7h0n"
        + "HE6Xfb\njqpXDpnBpvk4zjmzjjKYnIsUWTnADmu0fo3xTRqXi7g=\n"
        + "-----END SIGNATURE-----\npublished 2012-02-11 09:08:36");
  }

  @Test(expected = DescriptorParseException.class)
  public void testUnrecognizedLineFail()
      throws DescriptorParseException {
    String unrecognizedLine = "unrecognized-line 1";
    DescriptorBuilder.createWithUnrecognizedLine(unrecognizedLine, true);
  }

  @Test()
  public void testUnrecognizedLineIgnore()
      throws DescriptorParseException {
    String unrecognizedLine = "unrecognized-line 1";
    ExtraInfoDescriptor descriptor = DescriptorBuilder.
        createWithUnrecognizedLine(unrecognizedLine, false);
    List<String> unrecognizedLines = new ArrayList<String>();
    unrecognizedLines.add(unrecognizedLine);
    assertEquals(unrecognizedLines, descriptor.getUnrecognizedLines());
  }
}
