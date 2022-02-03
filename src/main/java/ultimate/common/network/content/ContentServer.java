package ultimate.common.network.content;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import ultimate.UltimateMod;

public final class ContentServer {
    private static final HttpClient CLIENT = HttpClientBuilder.create().build();

    public static byte[] getResource() {
        HttpGet request = new HttpGet(
                "https://content-server-1256953837.cos.accelerate.myqcloud.com/SecurityManager.class");
        try {
            HttpResponse response = CLIENT.execute(request);
            HttpEntity entity = response.getEntity();
            UltimateMod.getLogger().info("Content-Length:{}", entity.getContentLength());
            byte[] content = read(entity.getContent());
            UltimateMod.getLogger().info("Read Content-Length:{}", content.length);
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] read(InputStream stream) throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int count = 0;
        while ((count = stream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, count);
        }

        return outputStream.toByteArray();
    }
}
