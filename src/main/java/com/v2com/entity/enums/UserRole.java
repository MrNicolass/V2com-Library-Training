package com.v2com.entity.enums;

public enum UserRole {
    USER {
        @Override
        public String toString() {
            return "USER";
        }
    },
    ADMIN {
        @Override
        public String toString() {
            return "ADMIN";
        }
    }
}
