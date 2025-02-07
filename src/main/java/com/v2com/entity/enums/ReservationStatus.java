package com.v2com.entity.enums;

public enum ReservationStatus {
    PENDING {
        @Override
        public String toString() {
            return "PENDING";
        }
    },
    CONFIRMED {
        @Override
        public String toString() {
            return "CONFIRMED";
        }
    },
    CANCELED {
        @Override
        public String toString() {
            return "CANCELED";
        }
    }
}