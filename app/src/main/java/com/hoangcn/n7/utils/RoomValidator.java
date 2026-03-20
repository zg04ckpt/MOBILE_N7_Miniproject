package com.hoangcn.n7.utils;

public class RoomValidator {

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }

    public static boolean isValidPrice(String priceStr) {
        try {
            float price = Float.parseFloat(priceStr);
            return price > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidStatus(String status) {
        return "Còn trống".equals(status) || "Đã thuê".equals(status);
    }

    public static boolean isValidTenant(String tenant) {
        return tenant != null && !tenant.trim().isEmpty() && tenant.length() <= 100;
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phone.matches("^[0-9]{10,11}$");
    }

    public static String validateRoom(String name, String price, String status, String tenant, String phone) {
        if (!isValidName(name)) {
            return "Tên phòng không hợp lệ (1-50 ký tự)";
        }
        if (!isValidPrice(price)) {
            return "Giá thuê phải là số dương";
        }
        if (!isValidStatus(status)) {
            return "Tình trạng không hợp lệ";
        }
        if (!isValidTenant(tenant)) {
            return "Tên người thuê không hợp lệ (1-100 ký tự)";
        }
        if (!isValidPhoneNumber(phone)) {
            return "Số điện thoại phải là 10-11 chữ số";
        }
        return null;
    }
}
