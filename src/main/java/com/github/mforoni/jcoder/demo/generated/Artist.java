package com.github.mforoni.jcoder.demo.generated;

import com.google.common.base.Function;
import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Double;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Nullable;

/**
 * Auto generated from file artists.ods */
public class Artist implements Serializable {
	public static final Function<String[], Artist> ROW_TO_ARTIST = new Function<String[], Artist>() {
		@Override
		@Nullable
		public Artist apply(final String[] row) {
			return row == null ? null : parseRow(row);
		}
	};

	private double artistHotttnesss;

	private String artistId;

	private String artistName;

	private String artistMbtags;

	private String artistMbtagsCount;

	private String barsConfidence;

	private String barsStart;

	private String beatsConfidence;

	private String beatsStart;

	private String duration;

	private double endOfFadeIn;

	private Double familiarity;

	private double key;

	private double keyConfidence;

	private double latitude;

	private String location;

	private String longitude;

	private String loudness;

	private String mode;

	private double modeConfidence;

	private String releaseId;

	private String releaseName;

	private String similar;

	private String songHotttnesss;

	private String songId;

	private String startOfFadeOut;

	private String tatumsConfidence;

	private String tatumsStart;

	private Double tempo;

	private String terms;

	private String termsFreq;

	private String timeSignature;

	private String timeSignatureConfidence;

	private String title;

	private String year;

	private String field36;

	private String field37;

	private Double field38;

	private String field39;

	private Boolean field40;

	public Artist() {
	}

	@Nullable
	public static Artist parseRow(final String[] row) {
		final Artist artist = new Artist();
		for (int i = 0; i < row.length; i++) {
		final String obj = row[i];
		switch (i) {
		case 0:
		artist.setArtistHotttnesss(Double.parseDouble(obj));
		break;
		case 1:
		artist.setArtistId(obj);
		break;
		case 2:
		artist.setArtistName(obj);
		break;
		case 3:
		artist.setArtistMbtags(obj);
		break;
		case 4:
		artist.setArtistMbtagsCount(obj);
		break;
		case 5:
		artist.setBarsConfidence(obj);
		break;
		case 6:
		artist.setBarsStart(obj);
		break;
		case 7:
		artist.setBeatsConfidence(obj);
		break;
		case 8:
		artist.setBeatsStart(obj);
		break;
		case 9:
		artist.setDuration(obj);
		break;
		case 10:
		artist.setEndOfFadeIn(Double.parseDouble(obj));
		break;
		case 11:
		if (obj != null) {
		artist.setFamiliarity(Double.valueOf(obj));
		}
		;
		break;
		case 12:
		artist.setKey(Double.parseDouble(obj));
		break;
		case 13:
		artist.setKeyConfidence(Double.parseDouble(obj));
		break;
		case 14:
		artist.setLatitude(Double.parseDouble(obj));
		break;
		case 15:
		artist.setLocation(obj);
		break;
		case 16:
		artist.setLongitude(obj);
		break;
		case 17:
		artist.setLoudness(obj);
		break;
		case 18:
		artist.setMode(obj);
		break;
		case 19:
		artist.setModeConfidence(Double.parseDouble(obj));
		break;
		case 20:
		artist.setReleaseId(obj);
		break;
		case 21:
		artist.setReleaseName(obj);
		break;
		case 22:
		artist.setSimilar(obj);
		break;
		case 23:
		artist.setSongHotttnesss(obj);
		break;
		case 24:
		artist.setSongId(obj);
		break;
		case 25:
		artist.setStartOfFadeOut(obj);
		break;
		case 26:
		artist.setTatumsConfidence(obj);
		break;
		case 27:
		artist.setTatumsStart(obj);
		break;
		case 28:
		if (obj != null) {
		artist.setTempo(Double.valueOf(obj));
		}
		;
		break;
		case 29:
		artist.setTerms(obj);
		break;
		case 30:
		artist.setTermsFreq(obj);
		break;
		case 31:
		artist.setTimeSignature(obj);
		break;
		case 32:
		artist.setTimeSignatureConfidence(obj);
		break;
		case 33:
		artist.setTitle(obj);
		break;
		case 34:
		artist.setYear(obj);
		break;
		case 35:
		artist.setField36(obj);
		break;
		case 36:
		artist.setField37(obj);
		break;
		case 37:
		if (obj != null) {
		artist.setField38(Double.valueOf(obj));
		}
		;
		break;
		case 38:
		artist.setField39(obj);
		break;
		case 39:
		if (obj != null) {
		artist.setField40(Boolean.valueOf(obj));
		}
		;
		break;
		}
		;
		}
		return artist;
	}

	public double getArtistHotttnesss() {
		return artistHotttnesss;
	}

	public void setArtistHotttnesss(final double artistHotttnesss) {
		this.artistHotttnesss=artistHotttnesss;
	}

	public String getArtistId() {
		return artistId;
	}

	public void setArtistId(final String artistId) {
		this.artistId=artistId;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(final String artistName) {
		this.artistName=artistName;
	}

	public String getArtistMbtags() {
		return artistMbtags;
	}

	public void setArtistMbtags(final String artistMbtags) {
		this.artistMbtags=artistMbtags;
	}

	public String getArtistMbtagsCount() {
		return artistMbtagsCount;
	}

	public void setArtistMbtagsCount(final String artistMbtagsCount) {
		this.artistMbtagsCount=artistMbtagsCount;
	}

	public String getBarsConfidence() {
		return barsConfidence;
	}

	public void setBarsConfidence(final String barsConfidence) {
		this.barsConfidence=barsConfidence;
	}

	public String getBarsStart() {
		return barsStart;
	}

	public void setBarsStart(final String barsStart) {
		this.barsStart=barsStart;
	}

	public String getBeatsConfidence() {
		return beatsConfidence;
	}

	public void setBeatsConfidence(final String beatsConfidence) {
		this.beatsConfidence=beatsConfidence;
	}

	public String getBeatsStart() {
		return beatsStart;
	}

	public void setBeatsStart(final String beatsStart) {
		this.beatsStart=beatsStart;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(final String duration) {
		this.duration=duration;
	}

	public double getEndOfFadeIn() {
		return endOfFadeIn;
	}

	public void setEndOfFadeIn(final double endOfFadeIn) {
		this.endOfFadeIn=endOfFadeIn;
	}

	public Double getFamiliarity() {
		return familiarity;
	}

	public void setFamiliarity(final Double familiarity) {
		this.familiarity=familiarity;
	}

	public double getKey() {
		return key;
	}

	public void setKey(final double key) {
		this.key=key;
	}

	public double getKeyConfidence() {
		return keyConfidence;
	}

	public void setKeyConfidence(final double keyConfidence) {
		this.keyConfidence=keyConfidence;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(final double latitude) {
		this.latitude=latitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(final String location) {
		this.location=location;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(final String longitude) {
		this.longitude=longitude;
	}

	public String getLoudness() {
		return loudness;
	}

	public void setLoudness(final String loudness) {
		this.loudness=loudness;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(final String mode) {
		this.mode=mode;
	}

	public double getModeConfidence() {
		return modeConfidence;
	}

	public void setModeConfidence(final double modeConfidence) {
		this.modeConfidence=modeConfidence;
	}

	public String getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(final String releaseId) {
		this.releaseId=releaseId;
	}

	public String getReleaseName() {
		return releaseName;
	}

	public void setReleaseName(final String releaseName) {
		this.releaseName=releaseName;
	}

	public String getSimilar() {
		return similar;
	}

	public void setSimilar(final String similar) {
		this.similar=similar;
	}

	public String getSongHotttnesss() {
		return songHotttnesss;
	}

	public void setSongHotttnesss(final String songHotttnesss) {
		this.songHotttnesss=songHotttnesss;
	}

	public String getSongId() {
		return songId;
	}

	public void setSongId(final String songId) {
		this.songId=songId;
	}

	public String getStartOfFadeOut() {
		return startOfFadeOut;
	}

	public void setStartOfFadeOut(final String startOfFadeOut) {
		this.startOfFadeOut=startOfFadeOut;
	}

	public String getTatumsConfidence() {
		return tatumsConfidence;
	}

	public void setTatumsConfidence(final String tatumsConfidence) {
		this.tatumsConfidence=tatumsConfidence;
	}

	public String getTatumsStart() {
		return tatumsStart;
	}

	public void setTatumsStart(final String tatumsStart) {
		this.tatumsStart=tatumsStart;
	}

	public Double getTempo() {
		return tempo;
	}

	public void setTempo(final Double tempo) {
		this.tempo=tempo;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(final String terms) {
		this.terms=terms;
	}

	public String getTermsFreq() {
		return termsFreq;
	}

	public void setTermsFreq(final String termsFreq) {
		this.termsFreq=termsFreq;
	}

	public String getTimeSignature() {
		return timeSignature;
	}

	public void setTimeSignature(final String timeSignature) {
		this.timeSignature=timeSignature;
	}

	public String getTimeSignatureConfidence() {
		return timeSignatureConfidence;
	}

	public void setTimeSignatureConfidence(final String timeSignatureConfidence) {
		this.timeSignatureConfidence=timeSignatureConfidence;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title=title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(final String year) {
		this.year=year;
	}

	public String getField36() {
		return field36;
	}

	public void setField36(final String field36) {
		this.field36=field36;
	}

	public String getField37() {
		return field37;
	}

	public void setField37(final String field37) {
		this.field37=field37;
	}

	public Double getField38() {
		return field38;
	}

	public void setField38(final Double field38) {
		this.field38=field38;
	}

	public String getField39() {
		return field39;
	}

	public void setField39(final String field39) {
		this.field39=field39;
	}

	public Boolean isField40() {
		return field40;
	}

	public void setField40(final Boolean field40) {
		this.field40=field40;
	}

	@Override
	public String toString() {
		return String.format("Artist [artistHotttnesss=%s, artistId=%s, artistName=%s, artistMbtags=%s, artistMbtagsCount=%s, barsConfidence=%s, barsStart=%s, beatsConfidence=%s, beatsStart=%s, duration=%s, endOfFadeIn=%s, familiarity=%s, key=%s, keyConfidence=%s, latitude=%s, location=%s, longitude=%s, loudness=%s, mode=%s, modeConfidence=%s, releaseId=%s, releaseName=%s, similar=%s, songHotttnesss=%s, songId=%s, startOfFadeOut=%s, tatumsConfidence=%s, tatumsStart=%s, tempo=%s, terms=%s, termsFreq=%s, timeSignature=%s, timeSignatureConfidence=%s, title=%s, year=%s, field36=%s, field37=%s, field38=%s, field39=%s, field40=%s]", artistHotttnesss, artistId, artistName, artistMbtags, artistMbtagsCount, barsConfidence, barsStart, beatsConfidence, beatsStart, duration, endOfFadeIn, familiarity, key, keyConfidence, latitude, location, longitude, loudness, mode, modeConfidence, releaseId, releaseName, similar, songHotttnesss, songId, startOfFadeOut, tatumsConfidence, tatumsStart, tempo, terms, termsFreq, timeSignature, timeSignatureConfidence, title, year, field36, field37, field38, field39, field40);
	}
}
