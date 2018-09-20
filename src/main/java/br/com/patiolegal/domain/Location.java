package br.com.patiolegal.domain;

public class Location {

    private Shed shed;
    private String row;
    private String column;
    private String floor;

    public Shed getShed() {
        return shed;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }

    public String getFloor() {
        return floor;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String stringfy() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Barracão: ");
        stringBuilder.append(shed.getInitials());
        stringBuilder.append(" - ");
        stringBuilder.append(shed.getDescription());
        stringBuilder.append("\nFileira: ");
        stringBuilder.append(row);
        stringBuilder.append("\nColuna: ");
        stringBuilder.append(column);
        stringBuilder.append("\nAndar: ");
        stringBuilder.append(floor);
        return String.valueOf(stringBuilder);
    }

}
