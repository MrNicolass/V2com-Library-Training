package com.v2com.entity.enums;

public enum LoanStatus {
    ACTIVE {
        @Override
        public String toString() {
            return "ACTIVE";
        }
    },
    RETURNED {
        @Override
        public String toString() {
            return "RETURNED";
        }
    },
    LATE {
        @Override
        public String toString() {
            return "LATE";
        }
    }
}