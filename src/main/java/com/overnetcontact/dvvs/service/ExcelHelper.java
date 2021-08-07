package com.overnetcontact.dvvs.service;

import org.springframework.web.multipart.MultipartFile;

public final class ExcelHelper {

    private ExcelHelper() {}

    private static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }
}
