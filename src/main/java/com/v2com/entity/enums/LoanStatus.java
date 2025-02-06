package com.v2com.entity.enums;

public enum LoanStatus {
    ACTIVE {
        @Override
        public String toString() {
            return "USER";
        }
    },
    RETURNED {
        @Override
        public String toString() {
            return "ADMIN";
        }
    },
    LATE {
        @Override
        public String toString() {
            return "ADMIN";
        }
    }
}