package shop.mtcoding.jobara.util;

public class Parse {

    public static int careerToInt(String career) {
        int parseCareer = -99;

        switch (career) {
            case "신입":
                parseCareer = 0;
                break;
            case "1년이상 ~ 3년미만":
                parseCareer = 1;
                break;
            case "3년이상 ~ 5년미만":
                parseCareer = 3;
                break;
            case "6년이상":
                parseCareer = 6;
                break;
            case "경력무관":
                parseCareer = -1;
                break;
        }

        return parseCareer;
    }
}
