package ultimate.common.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import ultimate.UltimateMod;

public final class SecurityManager {
    private SecurityManager() {
    }

    public static void validate(File file) throws IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream("crc-32.ultimate.dat");
        ByteBuf bytebuf = ByteBufAllocator.DEFAULT.buffer();
        if (stream != null) {
            bytebuf.writeBytes(readInputStream(stream));
        }

        ProgressBar top = ProgressManager.push("SecurityManager", 2);
        top.step("Counting");
        try (JarFile jar = new JarFile(file)) {
            List<JarEntry> entries = Lists.newArrayList();
            Enumeration<JarEntry> enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();
                entries.add(entry);
            }

            top.step("Validating");
            ProgressBar current = ProgressManager.push("Validating", entries.size());
            for (JarEntry entry : entries) {
                String name = entry.getName();
                current.step(name);
                if (entry.isDirectory()) {
                    continue;
                }

                long crc = entry.getCrc();
                if (stream == null) {
                    bytebuf.writeLong(crc);
                } else if (!name.equals("crc-32.ultimate.dat")) {
                    long expected = bytebuf.readLong();
                    if (name.equals("ultimate/common/security/SecurityManager.class")) {
                        continue;
                    }

                    if (crc == -1 || crc != expected) {
                        UltimateMod.getLogger().fatal("Invalid file detected.");
                        UltimateMod.getLogger().fatal("File name:{}", entry.getName());
                        UltimateMod.getLogger().fatal("File CRC-32:{}", crc);
                        UltimateMod.getLogger().fatal("Expected CRC-32:{}", expected);
                        FMLCommonHandler.instance().exitJava(0, true);
                        throw new RuntimeException();
                    }
                }
            }

            ProgressManager.pop(current);
        }

        ProgressManager.pop(top);
        if (stream == null) {
            File crcFile = new File(file.getParent(), "crc-32.ultimate.dat");
            if (crcFile.exists()) {
                crcFile.delete();
            }

            if (crcFile.createNewFile()) {
                try (OutputStream out = new FileOutputStream(crcFile)) {
                    out.write(ByteBufUtil.getBytes(bytebuf));
                }
            }

            FMLCommonHandler.instance().exitJava(0, true);
            throw new RuntimeException();
        }
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int length = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((length = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, length);
        }

        bos.close();
        return bos.toByteArray();
    }
}
