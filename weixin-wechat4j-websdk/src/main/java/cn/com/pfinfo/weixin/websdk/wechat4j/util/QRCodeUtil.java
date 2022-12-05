package cn.com.pfinfo.weixin.websdk.wechat4j.util;

import cn.com.pfinfo.weixin.websdk.wechat4j.enums.OperatingSystem;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 二维码工具类
 *
 * @auther cuitpanfei
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class QRCodeUtil {
    private static final Log log = LogFactory.get(QRCodeUtil.class);
    private QRCodeUtil() {
    }

    /**
     * 从BitMatrix中得到boolean矩阵（不去除周围空白部分）
     *
     * @return 得到的boolean矩阵
     */
    private static boolean[][] toBoolMatrix(BitMatrix matrix) {
        return toBoolMatrix(matrix, false);
    }

    /**
     * 从BitMatrix中得到boolean矩阵
     *
     * @param matrix   BitMatrix
     * @param noMargin 是否去除周围空白
     * @return 得到的boolean矩阵
     */
    private static boolean[][] toBoolMatrix(BitMatrix matrix, boolean noMargin) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int top = 0, left = 0, bottom = 0, right = 0;
        if (noMargin) {
            int[] tl = matrix.getTopLeftOnBit();
            top = tl[0];
            left = tl[1];
            int[] br = matrix.getBottomRightOnBit();
            bottom = height - br[0] - 1;
            right = width - br[1] - 1;
        }
        boolean[][] m = new boolean[height - top - bottom][width - left - right];
        for (int h = top, i = 0; h < height - bottom; h++, i++) {
            for (int w = left, j = 0; w < width - right; w++, j++) {
                m[i][j] = matrix.get(w, h);
            }
        }
        return m;
    }

    /**
     * 将矩阵逆时针转90度
     *
     * @param matrix 旋转前的矩阵
     * @return 旋转后的矩阵
     */
    private static boolean[][] reverseMatrix(boolean[][] matrix) {
        if (matrix == null) {
            return null;
        }

        int height = matrix.length;
        int width = matrix[0].length;

        boolean[][] matrix2 = new boolean[width][height];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                matrix2[width - 1 - w][h] = matrix[h][w];
            }
        }

        return matrix2;
    }

    /**
     * 从boolMatrix左上角判断二维码定位标记的大小
     *
     * @param boolMatrix bool矩阵
     * @return 定位标记大小
     */
    private static int getBitCharSize(boolean[][] boolMatrix) {
        int a = 0, b = 0;
        out:
        for (boolean[] boolArr : boolMatrix) {
            boolean find = false;
            for (int i2 = 0, len2 = boolArr.length; i2 < len2; i2++) {
                if (!find && boolArr[i2]) {
                    find = true;
                    a = i2;
                }
                if (find && !boolArr[i2]) {
                    b = i2;
                    break out;
                }
            }
        }

        return (b - a);
    }

    /**
     * 从boolMatrix判断bit-char占位比
     *
     * @param boolMatrix bool矩阵
     * @return 占位比
     */
    private static int getBitCharRatio(boolean[][] boolMatrix) {
        int len = 4;
        // 找出四个角的占位数
        int[] size = new int[len];
        size[0] = getBitCharSize(boolMatrix);
        for (int i = 1; i < len; i++) {
            boolMatrix = reverseMatrix(boolMatrix);
            size[i] = getBitCharSize(boolMatrix);
        }
        // 统计每个占位数出现的次数
        int[] num = new int[len];
        for (int i = 0; i < len; i++) {
            int n = 0;
            for (int s : size) {
                if (s == size[i]) {
                    n++;
                }
            }
            num[i] = n;
        }
        // 找出最多的次数
        int maxNum = num[0];
        for (int i = 1; i < len; i++) {
            maxNum = Math.max(maxNum, num[i]);
        }
        // 找出出现次数最多的占位数
        int s = 0;
        for (int i = 0; i < len; i++) {
            if (num[i] == maxNum) {
                s = size[i];
            }
        }

        return s / 7;
    }

    /**
     * 将二维码图片转为字符矩阵
     *
     * @param image  二维码图片
     * @param onStr  实体字符串
     * @param offStr 空白字符串
     * @return 字符矩阵
     */
    public static String toCharMatrix(BufferedImage image, String onStr, String offStr) {
        String source = QrCodeUtil.decode(image);
        BitMatrix matrix = QrCodeUtil.encode(source,30,30);
        boolean[][] boolMatrix = toBoolMatrix(matrix, true);
        int ratio = getBitCharRatio(boolMatrix);
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = boolMatrix.length; i < len; i += ratio) {
            boolean[] boolArr = boolMatrix[i];
            for (int i2 = 0, len2 = boolArr.length; i2 < len2; i2 += ratio) {
                sb.append(boolArr[i2] ? onStr : offStr);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * 将二维码图片转为字符矩阵
     *
     * @param image 二维码图片
     * @return 字符矩阵
     */
    public static String toCharMatrix(BufferedImage image) {
        return toCharMatrix(image, "  ", "██");
    }

    /**
     * 将二维码图片转为字符矩阵
     *
     * @param data   二维码图片的字节数据
     * @param onStr  实体字符串
     * @param offStr 空白字符串
     * @return 字符矩阵
     */
    public static String toCharMatrix(byte[] data, String onStr, String offStr) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)){
            BufferedImage image = ImageIO.read(bais);
            return QRCodeUtil.toCharMatrix(image, onStr, offStr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将二维码图片转为字符矩阵
     *
     * @param data 二维码图片的字节数据
     * @return 字符矩阵
     */
    public static String toCharMatrix(byte[] data) {
        return toCharMatrix(data, "  ", "██");
    }

    /**
     * 将二维码图片数据写入到临时文件
     *
     * @param data 二维码图片的字节数据
     * @return 字符矩阵
     */
    public static File writeToTempFile(byte[] data) {
        FileOutputStream fos = null;
        try {
            File tmp = File.createTempFile(WechatApp.appSetting.get("wechat4j.qrcode.tmpfile.prefix"), ".jpg");
            fos = new FileOutputStream(tmp);
            fos.write(data);
            fos.flush();
            return tmp;
        } catch (IOException e) {
            log.error("二维码写入临时文件异常", e);
            throw new RuntimeException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 打开二维码图片
     *
     * @param data 二维码图片的字节数据
     */
    public static void openQRCodeImage(byte[] data) {
        OperatingSystem os = OperatingSystem.currentOperatingSystem();
        Runtime runtime;
        File tmp;
        switch (os) {
            case WINDOWS:
                runtime = Runtime.getRuntime();
                tmp = writeToTempFile(data);
                try {
                    runtime.exec("cmd /c start " + tmp.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MAC_OS:
                runtime = Runtime.getRuntime();
                tmp = writeToTempFile(data);
                try {
                    runtime.exec("open " + tmp.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}