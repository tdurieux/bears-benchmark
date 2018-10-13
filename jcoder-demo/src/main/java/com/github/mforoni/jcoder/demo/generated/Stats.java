package com.github.mforoni.jcoder.demo.generated;

import java.io.Serializable;
import javax.annotation.Nullable;
import com.google.common.base.Function;

/**
 * Auto generated from file Stats_2015-16.xlsx
 */
public class Stats implements Serializable {
  public static final Function<String[], Stats> ROW_TO_STATS = new Function<String[], Stats>() {
    @Override
    @Nullable
    public Stats apply(final String[] row) {
      return row == null ? null : parseRow(row);
    }
  };
  private int id;
  private String r;
  private String nome;
  private String squadra;
  private int pg;
  private double mv;
  private double mf;
  private int gf;
  private int gs;
  private int rp;
  private int rc;
  private int rplus;
  private int rminus;
  private int ass;
  private int asf;
  private int amm;
  private int esp;
  private int au;

  public Stats() {}

  @Nullable
  public static Stats parseRow(final String[] row) {
    final Stats stats = new Stats();
    for (int i = 0; i < row.length; i++) {
      final String obj = row[i];
      switch (i) {
        case 0:
          stats.setId(Integer.parseInt(obj));
          break;
        case 1:
          stats.setR(obj);
          break;
        case 2:
          stats.setNome(obj);
          break;
        case 3:
          stats.setSquadra(obj);
          break;
        case 4:
          stats.setPg(Integer.parseInt(obj));
          break;
        case 5:
          stats.setMv(Double.parseDouble(obj));
          break;
        case 6:
          stats.setMf(Double.parseDouble(obj));
          break;
        case 7:
          stats.setGf(Integer.parseInt(obj));
          break;
        case 8:
          stats.setGs(Integer.parseInt(obj));
          break;
        case 9:
          stats.setRp(Integer.parseInt(obj));
          break;
        case 10:
          stats.setRc(Integer.parseInt(obj));
          break;
        case 11:
          stats.setRplus(Integer.parseInt(obj));
          break;
        case 12:
          stats.setRminus(Integer.parseInt(obj));
          break;
        case 13:
          stats.setAss(Integer.parseInt(obj));
          break;
        case 14:
          stats.setAsf(Integer.parseInt(obj));
          break;
        case 15:
          stats.setAmm(Integer.parseInt(obj));
          break;
        case 16:
          stats.setEsp(Integer.parseInt(obj));
          break;
        case 17:
          stats.setAu(Integer.parseInt(obj));
          break;
      };
    }
    return stats;
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getR() {
    return r;
  }

  public void setR(final String r) {
    this.r = r;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(final String nome) {
    this.nome = nome;
  }

  public String getSquadra() {
    return squadra;
  }

  public void setSquadra(final String squadra) {
    this.squadra = squadra;
  }

  public int getPg() {
    return pg;
  }

  public void setPg(final int pg) {
    this.pg = pg;
  }

  public double getMv() {
    return mv;
  }

  public void setMv(final double mv) {
    this.mv = mv;
  }

  public double getMf() {
    return mf;
  }

  public void setMf(final double mf) {
    this.mf = mf;
  }

  public int getGf() {
    return gf;
  }

  public void setGf(final int gf) {
    this.gf = gf;
  }

  public int getGs() {
    return gs;
  }

  public void setGs(final int gs) {
    this.gs = gs;
  }

  public int getRp() {
    return rp;
  }

  public void setRp(final int rp) {
    this.rp = rp;
  }

  public int getRc() {
    return rc;
  }

  public void setRc(final int rc) {
    this.rc = rc;
  }

  public int getRplus() {
    return rplus;
  }

  public void setRplus(final int rplus) {
    this.rplus = rplus;
  }

  public int getRminus() {
    return rminus;
  }

  public void setRminus(final int rminus) {
    this.rminus = rminus;
  }

  public int getAss() {
    return ass;
  }

  public void setAss(final int ass) {
    this.ass = ass;
  }

  public int getAsf() {
    return asf;
  }

  public void setAsf(final int asf) {
    this.asf = asf;
  }

  public int getAmm() {
    return amm;
  }

  public void setAmm(final int amm) {
    this.amm = amm;
  }

  public int getEsp() {
    return esp;
  }

  public void setEsp(final int esp) {
    this.esp = esp;
  }

  public int getAu() {
    return au;
  }

  public void setAu(final int au) {
    this.au = au;
  }

  @Override
  public String toString() {
    return String.format(
        "Stats [id=%s, r=%s, nome=%s, squadra=%s, pg=%s, mv=%s, mf=%s, gf=%s, gs=%s, rp=%s, rc=%s, rplus=%s, rminus=%s, ass=%s, asf=%s, amm=%s, esp=%s, au=%s]",
        id, r, nome, squadra, pg, mv, mf, gf, gs, rp, rc, rplus, rminus, ass, asf, amm, esp, au);
  }
}
