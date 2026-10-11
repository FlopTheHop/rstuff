/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.NativeImage
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.ResourceManagerReloadListener
 *  net.neoforged.fml.loading.FMLPaths
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package org.ExtraTortik.ricks_stuff.client.portal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.imageio.metadata.IIOMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CustomPortalTextureManager {
    private static final Logger LOGGER = LogManager.getLogger(CustomPortalTextureManager.class);
    public static final ResourceLocation CUSTOM_GREEN_PORTAL_LOC = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"dynamic/custom_green_portal");
    private static DynamicTexture dynamicTexture = null;
    private static boolean hasCustomTexture = false;
    private static int totalFrames = 40;
    private static float animSpeed = 0.8f;
    private static final Comparator<Path> NATURAL_PATH_ORDER = (p1, p2) -> {
        String s1 = p1.getFileName().toString();
        String s2 = p2.getFileName().toString();
        Pattern p = Pattern.compile("(\\d+)|(\\D+)");
        Matcher m1 = p.matcher(s1);
        Matcher m2 = p.matcher(s2);
        while (m1.find()) {
            if (!m2.find()) return Integer.compare(s1.length(), s2.length());
            String g1 = m1.group();
            String g2 = m2.group();
            if (Character.isDigit(g1.charAt(0)) && Character.isDigit(g2.charAt(0))) {
                long n2;
                long n1 = Long.parseLong(g1);
                int cmp = Long.compare(n1, n2 = Long.parseLong(g2));
                if (cmp == 0) continue;
                return cmp;
            }
            int cmp = g1.compareToIgnoreCase(g2);
            if (cmp != 0) return cmp;
        }
        return Integer.compare(s1.length(), s2.length());
    };

    public static Path getPortalDirectory() {
        return FMLPaths.CONFIGDIR.get().resolve("ricks_stuff").resolve("green_portal");
    }

    public static void init() {
        try {
            Path readme;
            Path dir = CustomPortalTextureManager.getPortalDirectory();
            if (!Files.exists(dir, new LinkOption[0])) {
                Files.createDirectories(dir, new FileAttribute[0]);
            }
            if (!Files.exists(readme = dir.resolve("README.txt"), new LinkOption[0])) {
                String text = "Custom Green Portal Texture Guide\n\nDrop your custom image or animation into this folder to override the default green portal texture in-game.\nPress F3 + T in-game to reload textures without restarting Minecraft.\n\nSupported formats:\n1. Single Image:\n   Place a single .png or .jpg file.\n\n2. Vertical Sprite Sheet:\n   Place a .png where height is an exact multiple of width (e.g. 200x8000 for 40 frames).\n   Frames play from top to bottom.\n\n3. Frame Sequence:\n   Place multiple .png files numbered in order (e.g. 0.png, 1.png, 2.png or frame_0.png, frame_1.png).\n\n4. Animated GIF:\n   Place a .gif file. Frame delays are parsed automatically.\n\nOptional Configuration (portal.json):\nCreate a portal.json file in this folder to customize animation timing:\n{\n  \"fps\": 20,\n  \"speed\": 0.8\n}\n\nTo restore the default texture, delete all image files from this folder and press F3 + T.\n";
                Files.writeString((Path)readme, (CharSequence)text, (Charset)StandardCharsets.UTF_8, (OpenOption[])new OpenOption[0]);
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to initialize custom portal directory", (Throwable)e);
        }
        CustomPortalTextureManager.reload();
    }

    public static ResourceManagerReloadListener createReloadListener() {
        return resourceManager -> CustomPortalTextureManager.reload();
    }

    public static synchronized void reload() {
        Path dir = CustomPortalTextureManager.getPortalDirectory();
        if (!Files.exists(dir, new LinkOption[0])) {
            CustomPortalTextureManager.clearTexture();
            return;
        }
        try {
            Float customFps = null;
            Float customSpeed = null;
            Path configJson = dir.resolve("portal.json");
            if (Files.exists(configJson, new LinkOption[0])) {
                try (BufferedReader reader = Files.newBufferedReader(configJson, StandardCharsets.UTF_8);){
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    if (obj.has("fps")) {
                        customFps = Float.valueOf(obj.get("fps").getAsFloat());
                    }
                    if (obj.has("speed")) {
                        customSpeed = Float.valueOf(obj.get("speed").getAsFloat());
                    }
                }
                catch (Exception e) {
                    LOGGER.warn("Failed to parse portal.json", (Throwable)e);
                }
            }
            ArrayList<Path> imageFiles = new ArrayList<Path>();
            ArrayList<Path> gifFiles = new ArrayList<Path>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir);){
                for (Path entry : stream) {
                    if (!Files.isRegularFile(entry, new LinkOption[0])) continue;
                    String name = entry.getFileName().toString().toLowerCase(Locale.ROOT);
                    if (name.endsWith(".gif")) {
                        gifFiles.add(entry);
                        continue;
                    }
                    if (!name.endsWith(".png") && !name.endsWith(".jpg") && !name.endsWith(".jpeg")) continue;
                    imageFiles.add(entry);
                }
            }
            if (gifFiles.isEmpty() && imageFiles.isEmpty()) {
                CustomPortalTextureManager.clearTexture();
                LOGGER.info("Custom green portal directory is empty; using default portal texture.");
                return;
            }
            if (!gifFiles.isEmpty()) {
                Path gifPath = (Path)gifFiles.get(0);
                LOGGER.info("Loading custom green portal GIF: {}", (Object)gifPath.getFileName());
                CustomPortalTextureManager.loadGif(gifPath, customFps, customSpeed);
                return;
            }
            if (imageFiles.size() > 1) {
                imageFiles.sort(NATURAL_PATH_ORDER);
                LOGGER.info("Loading custom green portal frame sequence ({} frames)", (Object)imageFiles.size());
                CustomPortalTextureManager.loadFrameSequence(imageFiles, customFps, customSpeed);
                return;
            }
            Path singleFile = (Path)imageFiles.get(0);
            LOGGER.info("Loading custom green portal image: {}", (Object)singleFile.getFileName());
            CustomPortalTextureManager.loadSingleImage(singleFile, customFps, customSpeed);
        }
        catch (Exception e) {
            LOGGER.error("Failed to load custom green portal texture, falling back to default", (Throwable)e);
            CustomPortalTextureManager.clearTexture();
        }
    }

    private static void loadSingleImage(Path filePath, Float customFps, Float customSpeed) throws IOException {
        try (InputStream is = Files.newInputStream(filePath, new OpenOption[0]);){
            NativeImage nativeImage = NativeImage.read((InputStream)is);
            int width = nativeImage.getWidth();
            int height = nativeImage.getHeight();
            int frames = 1;
            if (height > width && height % width == 0) {
                frames = height / width;
            }
            CustomPortalTextureManager.applyTexture(nativeImage, frames, customFps != null ? customFps.floatValue() / 25.0f : (customSpeed != null ? customSpeed.floatValue() : 0.8f));
            LOGGER.info("Loaded single custom portal texture: {}x{}, detected {} frame(s)", (Object)width, (Object)height, (Object)frames);
        }
    }

    private static void loadFrameSequence(List<Path> files, Float customFps, Float customSpeed) throws IOException {
        ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();
        int maxWidth = 0;
        int maxHeight = 0;
        for (Path file : files) {
            InputStream is = Files.newInputStream(file, new OpenOption[0]);
            try {
                BufferedImage img = ImageIO.read(is);
                if (img == null) continue;
                frames.add(img);
                maxWidth = Math.max(maxWidth, img.getWidth());
                maxHeight = Math.max(maxHeight, img.getHeight());
            }
            finally {
                if (is == null) continue;
                is.close();
            }
        }
        if (frames.isEmpty()) throw new IOException("No valid frames could be decoded from sequence");
        if (maxWidth <= 0) throw new IOException("No valid frames could be decoded from sequence");
        if (maxHeight <= 0) {
            throw new IOException("No valid frames could be decoded from sequence");
        }
        int totalHeight = maxHeight * frames.size();
        NativeImage stitched = new NativeImage(maxWidth, totalHeight, false);
        int i = 0;
        while (true) {
            int yOffset;
            BufferedImage frame;
            if (i < frames.size()) {
                frame = (BufferedImage)frames.get(i);
                yOffset = i * maxHeight;
            } else {
                float speed = customFps != null ? customFps.floatValue() / 25.0f : (customSpeed != null ? customSpeed.floatValue() : 0.8f);
                CustomPortalTextureManager.applyTexture(stitched, frames.size(), speed);
                LOGGER.info("Stitched frame sequence: {}x{}, {} frames", (Object)maxWidth, (Object)totalHeight, (Object)frames.size());
                return;
            }
            for (int x = 0; x < maxWidth; ++x) {
                for (int y = 0; y < maxHeight; ++y) {
                    int srcX = x * frame.getWidth() / maxWidth;
                    int srcY = y * frame.getHeight() / maxHeight;
                    int argb = frame.getRGB(srcX, srcY);
                    int a = argb >> 24 & 0xFF;
                    int r = argb >> 16 & 0xFF;
                    int g = argb >> 8 & 0xFF;
                    int b = argb & 0xFF;
                    int abgr = a << 24 | b << 16 | g << 8 | r;
                    stitched.setPixelRGBA(x, yOffset + y, abgr);
                }
            }
            ++i;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    private static void loadGif(Path gifPath, Float customFps, Float customSpeed) throws IOException {
        block17: {
            block16: {
                readers = ImageIO.getImageReadersByFormatName("gif");
                if (!readers.hasNext()) {
                    throw new IOException("No GIF image reader available in Java runtime");
                }
                reader = readers.next();
                stream = ImageIO.createImageInputStream(Files.newInputStream(gifPath, new OpenOption[0]));
                reader.setInput(stream);
                numFrames = reader.getNumImages(true);
                if (numFrames <= 0) {
                    throw new IOException("GIF file contains no frames");
                }
                width = reader.getWidth(0);
                height = reader.getHeight(0);
                master = new BufferedImage(width, height, 2);
                g2d = master.createGraphics();
                frameList = new ArrayList<BufferedImage>();
                totalDelayMs = 0;
                for (i = 0; i < numFrames; totalDelayMs += frameDelayMs, ++i) {
                    rawFrame = reader.read(i);
                    frameDelayMs = 50;
                    metadata = reader.getImageMetadata(i);
                    if (metadata != null) {
                        frameDelayMs = CustomPortalTextureManager.extractGifFrameDelay(metadata, frameDelayMs);
                    }
                    g2d.drawImage((Image)rawFrame, 0, 0, null);
                    frameCopy = new BufferedImage(width, height, 2);
                    copyG = frameCopy.createGraphics();
                    copyG.drawImage((Image)master, 0, 0, null);
                    copyG.dispose();
                    frameList.add(frameCopy);
                }
                g2d.dispose();
                totalHeight = height * frameList.size();
                stitched = new NativeImage(width, totalHeight, false);
                i = 0;
lbl38:
                // 2 sources

                while (true) {
                    if (i >= frameList.size()) break block16;
                    frame = (BufferedImage)frameList.get(i);
                    yOffset = i * height;
                    break block17;
                    break;
                }
                finally {
                    if (stream != null) {
                        stream.close();
                    }
                }
                finally {
                    reader.dispose();
                }
            }
            avgDelaySec = (float)totalDelayMs / (float)numFrames / 1000.0f;
            v0 = calcFps = avgDelaySec > 0.0f ? 1.0f / avgDelaySec : 20.0f;
            speed = customFps != null ? customFps.floatValue() / 25.0f : (customSpeed != null ? customSpeed.floatValue() : calcFps / 25.0f);
            CustomPortalTextureManager.applyTexture(stitched, frameList.size(), speed);
            CustomPortalTextureManager.LOGGER.info("Decoded GIF: {}x{}, {} frames, speed={}", (Object)width, (Object)totalHeight, (Object)frameList.size(), (Object)Float.valueOf(speed));
            return;
        }
        for (x = 0; x < width; ++x) {
            for (y = 0; y < height; ++y) {
                argb = frame.getRGB(x, y);
                a = argb >> 24 & 255;
                r = argb >> 16 & 255;
                g = argb >> 8 & 255;
                b = argb & 255;
                abgr = a << 24 | b << 16 | g << 8 | r;
                stitched.setPixelRGBA(x, yOffset + y, abgr);
            }
        }
        ++i;
        ** continue;
    }

    private static int extractGifFrameDelay(IIOMetadata metadata, int defaultDelay) {
        try {
            String[] names;
            String[] stringArray = names = metadata.getMetadataFormatNames();
            int n = stringArray.length;
            int n2 = 0;
            while (n2 < n) {
                String name = stringArray[n2];
                if ("javax_imageio_gif_image_1.0".equalsIgnoreCase(name)) {
                    Node root = metadata.getAsTree(name);
                    NodeList children = root.getChildNodes();
                    for (int j = 0; j < children.getLength(); ++j) {
                        int delayHundredths;
                        NamedNodeMap attr;
                        Node delayNode;
                        Node node = children.item(j);
                        if (!"GraphicControlExtension".equalsIgnoreCase(node.getNodeName()) || (delayNode = (attr = node.getAttributes()).getNamedItem("delayTime")) == null || (delayHundredths = Integer.parseInt(delayNode.getNodeValue())) <= 0) continue;
                        return delayHundredths * 10;
                    }
                }
                ++n2;
            }
            return defaultDelay;
        }
        catch (Exception exception) {
            // empty catch block
        }
        return defaultDelay;
    }

    private static synchronized void applyTexture(NativeImage nativeImage, int frames, float speed) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            if (dynamicTexture != null) {
                mc.getTextureManager().release(CUSTOM_GREEN_PORTAL_LOC);
                dynamicTexture.close();
            }
            dynamicTexture = new DynamicTexture(nativeImage);
            mc.getTextureManager().register(CUSTOM_GREEN_PORTAL_LOC, (AbstractTexture)dynamicTexture);
            totalFrames = Math.max(1, frames);
            animSpeed = Math.max(0.01f, speed);
            hasCustomTexture = true;
        });
    }

    private static synchronized void clearTexture() {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            if (dynamicTexture != null) {
                mc.getTextureManager().release(CUSTOM_GREEN_PORTAL_LOC);
                dynamicTexture.close();
                dynamicTexture = null;
            }
            hasCustomTexture = false;
            totalFrames = 40;
            animSpeed = 0.8f;
        });
    }

    public static boolean hasCustomTexture() {
        return hasCustomTexture;
    }

    public static ResourceLocation getTextureLocation() {
        return CUSTOM_GREEN_PORTAL_LOC;
    }

    public static int getTotalFrames() {
        return totalFrames;
    }

    public static float getAnimSpeed() {
        return animSpeed;
    }
}
