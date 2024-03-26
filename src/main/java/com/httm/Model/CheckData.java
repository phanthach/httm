package com.httm.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "checkdata")
public class CheckData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "soluong")
    private long soluong;

    public CheckData() {
    }

    public CheckData(int id, long soluong) {
        this.id = id;
        this.soluong = soluong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSoluong() {
        return soluong;
    }

    public void setSoluong(long soluong) {
        this.soluong = soluong;
    }

    @Override
    public String toString() {
        return "CheckData{" +
                "id=" + id +
                ", soluong=" + soluong +
                '}';
    }
}
