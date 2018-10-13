package com.github.mforoni.jcoder.demo.generated;

import java.io.Serializable;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Auto generated from file music.csv
 */
public class Music implements Serializable {
  public static final CellProcessor[] CELL_PROCESSOR = { //
      new NotNull(new ParseDouble()), // artistHotttnesss
      new NotNull(), // artistId
      new NotNull(), // artistName
      new Optional(), // artistMbtags
      new NotNull(new ParseDouble()), // artistMbtagsCount
      new NotNull(new ParseDouble()), // barsConfidence
      new NotNull(new ParseDouble()), // barsStart
      new NotNull(new ParseDouble()), // beatsConfidence
      new NotNull(new ParseDouble()), // beatsStart
      new NotNull(new ParseDouble()), // duration
      new NotNull(new ParseDouble()), // endOfFadeIn
      new Optional(new ParseDouble()), // familiarity
      new NotNull(new ParseDouble()), // key
      new NotNull(new ParseDouble()), // keyConfidence
      new NotNull(new ParseDouble()), // latitude
      new NotNull(), // location
      new NotNull(new ParseDouble()), // longitude
      new NotNull(new ParseDouble()), // loudness
      new NotNull(new ParseBool()), // mode
      new NotNull(new ParseDouble()), // modeConfidence
      new NotNull(), // releaseId
      new NotNull(), // releaseName
      new NotNull(), // similar
      new Optional(new ParseDouble()), // songHotttnesss
      new NotNull(), // songId
      new NotNull(new ParseDouble()), // startOfFadeOut
      new NotNull(new ParseDouble()), // tatumsConfidence
      new NotNull(new ParseDouble()), // tatumsStart
      new NotNull(new ParseDouble()), // tempo
      new Optional(), // terms
      new NotNull(new ParseDouble()), // termsFreq
      new NotNull(new ParseDouble()), // timeSignature
      new NotNull(new ParseDouble()), // timeSignatureConfidence
      new Optional(), // title
      new NotNull(new ParseInt()), // year
  };
  private double artistHotttnesss;
  private String artistId;
  private String artistName;
  private String artistMbtags;
  private double artistMbtagsCount;
  private double barsConfidence;
  private double barsStart;
  private double beatsConfidence;
  private double beatsStart;
  private double duration;
  private double endOfFadeIn;
  private Double familiarity;
  private double key;
  private double keyConfidence;
  private double latitude;
  private String location;
  private double longitude;
  private double loudness;
  private boolean mode;
  private double modeConfidence;
  private String releaseId;
  private String releaseName;
  private String similar;
  private Double songHotttnesss;
  private String songId;
  private double startOfFadeOut;
  private double tatumsConfidence;
  private double tatumsStart;
  private double tempo;
  private String terms;
  private double termsFreq;
  private double timeSignature;
  private double timeSignatureConfidence;
  private String title;
  private int year;

  public Music() {}

  public double getArtistHotttnesss() {
    return artistHotttnesss;
  }

  public void setArtistHotttnesss(final double artistHotttnesss) {
    this.artistHotttnesss = artistHotttnesss;
  }

  public String getArtistId() {
    return artistId;
  }

  public void setArtistId(final String artistId) {
    this.artistId = artistId;
  }

  public String getArtistName() {
    return artistName;
  }

  public void setArtistName(final String artistName) {
    this.artistName = artistName;
  }

  public String getArtistMbtags() {
    return artistMbtags;
  }

  public void setArtistMbtags(final String artistMbtags) {
    this.artistMbtags = artistMbtags;
  }

  public double getArtistMbtagsCount() {
    return artistMbtagsCount;
  }

  public void setArtistMbtagsCount(final double artistMbtagsCount) {
    this.artistMbtagsCount = artistMbtagsCount;
  }

  public double getBarsConfidence() {
    return barsConfidence;
  }

  public void setBarsConfidence(final double barsConfidence) {
    this.barsConfidence = barsConfidence;
  }

  public double getBarsStart() {
    return barsStart;
  }

  public void setBarsStart(final double barsStart) {
    this.barsStart = barsStart;
  }

  public double getBeatsConfidence() {
    return beatsConfidence;
  }

  public void setBeatsConfidence(final double beatsConfidence) {
    this.beatsConfidence = beatsConfidence;
  }

  public double getBeatsStart() {
    return beatsStart;
  }

  public void setBeatsStart(final double beatsStart) {
    this.beatsStart = beatsStart;
  }

  public double getDuration() {
    return duration;
  }

  public void setDuration(final double duration) {
    this.duration = duration;
  }

  public double getEndOfFadeIn() {
    return endOfFadeIn;
  }

  public void setEndOfFadeIn(final double endOfFadeIn) {
    this.endOfFadeIn = endOfFadeIn;
  }

  public Double getFamiliarity() {
    return familiarity;
  }

  public void setFamiliarity(final Double familiarity) {
    this.familiarity = familiarity;
  }

  public double getKey() {
    return key;
  }

  public void setKey(final double key) {
    this.key = key;
  }

  public double getKeyConfidence() {
    return keyConfidence;
  }

  public void setKeyConfidence(final double keyConfidence) {
    this.keyConfidence = keyConfidence;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(final double latitude) {
    this.latitude = latitude;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(final double longitude) {
    this.longitude = longitude;
  }

  public double getLoudness() {
    return loudness;
  }

  public void setLoudness(final double loudness) {
    this.loudness = loudness;
  }

  public boolean isMode() {
    return mode;
  }

  public void setMode(final boolean mode) {
    this.mode = mode;
  }

  public double getModeConfidence() {
    return modeConfidence;
  }

  public void setModeConfidence(final double modeConfidence) {
    this.modeConfidence = modeConfidence;
  }

  public String getReleaseId() {
    return releaseId;
  }

  public void setReleaseId(final String releaseId) {
    this.releaseId = releaseId;
  }

  public String getReleaseName() {
    return releaseName;
  }

  public void setReleaseName(final String releaseName) {
    this.releaseName = releaseName;
  }

  public String getSimilar() {
    return similar;
  }

  public void setSimilar(final String similar) {
    this.similar = similar;
  }

  public Double getSongHotttnesss() {
    return songHotttnesss;
  }

  public void setSongHotttnesss(final Double songHotttnesss) {
    this.songHotttnesss = songHotttnesss;
  }

  public String getSongId() {
    return songId;
  }

  public void setSongId(final String songId) {
    this.songId = songId;
  }

  public double getStartOfFadeOut() {
    return startOfFadeOut;
  }

  public void setStartOfFadeOut(final double startOfFadeOut) {
    this.startOfFadeOut = startOfFadeOut;
  }

  public double getTatumsConfidence() {
    return tatumsConfidence;
  }

  public void setTatumsConfidence(final double tatumsConfidence) {
    this.tatumsConfidence = tatumsConfidence;
  }

  public double getTatumsStart() {
    return tatumsStart;
  }

  public void setTatumsStart(final double tatumsStart) {
    this.tatumsStart = tatumsStart;
  }

  public double getTempo() {
    return tempo;
  }

  public void setTempo(final double tempo) {
    this.tempo = tempo;
  }

  public String getTerms() {
    return terms;
  }

  public void setTerms(final String terms) {
    this.terms = terms;
  }

  public double getTermsFreq() {
    return termsFreq;
  }

  public void setTermsFreq(final double termsFreq) {
    this.termsFreq = termsFreq;
  }

  public double getTimeSignature() {
    return timeSignature;
  }

  public void setTimeSignature(final double timeSignature) {
    this.timeSignature = timeSignature;
  }

  public double getTimeSignatureConfidence() {
    return timeSignatureConfidence;
  }

  public void setTimeSignatureConfidence(final double timeSignatureConfidence) {
    this.timeSignatureConfidence = timeSignatureConfidence;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public int getYear() {
    return year;
  }

  public void setYear(final int year) {
    this.year = year;
  }

  @Override
  public String toString() {
    return String.format(
        "Music [artistHotttnesss=%s, artistId=%s, artistName=%s, artistMbtags=%s, artistMbtagsCount=%s, barsConfidence=%s, barsStart=%s, beatsConfidence=%s, beatsStart=%s, duration=%s, endOfFadeIn=%s, familiarity=%s, key=%s, keyConfidence=%s, latitude=%s, location=%s, longitude=%s, loudness=%s, mode=%s, modeConfidence=%s, releaseId=%s, releaseName=%s, similar=%s, songHotttnesss=%s, songId=%s, startOfFadeOut=%s, tatumsConfidence=%s, tatumsStart=%s, tempo=%s, terms=%s, termsFreq=%s, timeSignature=%s, timeSignatureConfidence=%s, title=%s, year=%s]",
        artistHotttnesss, artistId, artistName, artistMbtags, artistMbtagsCount, barsConfidence,
        barsStart, beatsConfidence, beatsStart, duration, endOfFadeIn, familiarity, key,
        keyConfidence, latitude, location, longitude, loudness, mode, modeConfidence, releaseId,
        releaseName, similar, songHotttnesss, songId, startOfFadeOut, tatumsConfidence, tatumsStart,
        tempo, terms, termsFreq, timeSignature, timeSignatureConfidence, title, year);
  }
}
