package com.codefromscratch.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {
    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass(){
        httpParser = new HttpParser();
    }
    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = HttpParser.parseHttpRequest(generateValidGETTestCase());
        } catch (HttpParsingException e) {
            fail(e);
        }

        assertNotNull(request);
        assertEquals(request.getMethod(),HttpMethod.GET);
        assertEquals(request.getRequestTarget(),"/");
        assertEquals(request.getOriginalHttpVersion(),"HTTP/1.1");
        assertEquals(request.getBestCompatibleHttpVersion(),HttpVersion.HTTP_1_1);
    }
    @Test
    void parseHttpRequestBadMethod1() {
        try {
            HttpRequest request =  HttpParser.parseHttpRequest(generateBadGETTestCase());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }
    @Test
    void parseHttpRequestBadMethod2() {
        try {
            HttpRequest request =  HttpParser.parseHttpRequest(generateBadGETTestCase2());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }
    @Test
    void parseHttpRequestInvalidNumberOfValues3() {
        try {
            HttpRequest request =  HttpParser.parseHttpRequest(generateBadGETTestCaseRequestInvalidNumberOfValues());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    @Test
    void parseHttpEmptyRequestLine4() {
        try {
            HttpRequest request =  HttpParser.parseHttpRequest(generateBadGETTestCaseEmptyRequestLine());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    @Test
    void parseHttpRequestLineOnlyCRnoLF() {
        try {
            HttpRequest request =  HttpParser.parseHttpRequest(generateBadGETTestCaseRequestLineOnlyCRnoLF());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestBadHttpVersion() {
        try {
            HttpRequest request =  HttpParser.parseHttpRequest(generateBadHttpVersionTestCase());

            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }
    @Test
    void parseHttpRequestUnsupportedHttpVersion() {
        try {
            HttpRequest request =  HttpParser.parseHttpRequest(generateUnsupportedHttpVersionTestCase());

            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }
    @Test
    void parseHttpRequestSupportedHttpVersion() {
        try {
            HttpRequest request =  HttpParser.parseHttpRequest(generateSupportedHttpVersion());
            assertNotNull(request);
            assertEquals(request.getBestCompatibleHttpVersion(),HttpVersion.HTTP_1_1);
            assertEquals(request.getOriginalHttpVersion(),"HTTP/1.2");
        } catch (HttpParsingException e) {
            fail();
        }
    }

    private InputStream generateValidGETTestCase(){
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Google Chrome\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ru-RU,ru;q=0.9"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadGETTestCase(){
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ru-RU,ru;q=0.9"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }
    private InputStream generateBadGETTestCase2(){
        String rawData = "GETTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ru-RU,ru;q=0.9"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }
    private InputStream generateBadGETTestCaseRequestInvalidNumberOfValues(){
        String rawData = "GET / AAAA HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ru-RU,ru;q=0.9"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }
    private InputStream generateBadGETTestCaseEmptyRequestLine(){
        String rawData = "\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ru-RU,ru;q=0.9"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadGETTestCaseRequestLineOnlyCRnoLF(){
        String rawData = "GET / AAAA HTTP/1.1\r" +   // <----no LF
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ru-RU,ru;q=0.9"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadHttpVersionTestCase() {
        String rawData = "GET / HTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Google Chrome\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ru-RU,ru;q=0.9"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateUnsupportedHttpVersionTestCase() {
        String rawData = "GET / HTTP/2.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Google Chrome\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ru-RU,ru;q=0.9"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateSupportedHttpVersion() {
        String rawData = "GET / HTTP/1.2\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Not A(Brand\";v=\"99\", \"Google Chrome\";v=\"121\", \"Chromium\";v=\"121\"\r\n" +
                "sec-ch-ua-mobile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: ru-RU,ru;q=0.9"+
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }
}