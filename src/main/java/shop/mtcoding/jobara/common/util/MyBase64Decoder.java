package shop.mtcoding.jobara.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import shop.mtcoding.jobara.common.ex.CustomApiException;

public class MyBase64Decoder {
    private static void checkExist(String base64Image) {
        if (base64Image.isEmpty()) {
            throw new CustomApiException("사진이 전송되지 않았습니다");
        }
    }

    private static byte[] decode(String base64Image) {
        String[] parts = base64Image.split(",");
        // base64Data : data:image/png;base64, 없앤 base64 String 값
        String base64Data = parts[1];
        // 하드 디스크는 이진데이터를 읽어 저장하므로 base64 문자셋 -> 이진 데이터 디코딩
        byte[] decodedData = Base64.getDecoder().decode(base64Data);
        return decodedData;
    }

    private static String checkImage(byte[] decodedData) throws IOException {
        String mimeType = null;
        mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(decodedData));

        if (!mimeType.startsWith("image/")) {
            throw new CustomApiException("사진 파일만 업로드 할 수 있습니다.");
        }
        return mimeType;
    }

    public static String saveImage(String base64Image) throws IOException {
        checkExist(base64Image);
        byte[] decodedData = decode(base64Image);
        String mimeType = checkImage(decodedData);
        String staticFolder = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\";
        UUID uuid = UUID.randomUUID();
        String filePath = "\\images\\" + uuid + "_" + System.currentTimeMillis() + "."
                + mimeType.split("/")[1];

        // filePath :
        // \images\ uuid값_시간.프로필사진.png
        Path imageFilePath = Paths.get(staticFolder + "\\" + filePath);
        Files.write(imageFilePath, decodedData);

        return imageFilePath.toString();
    }
}
