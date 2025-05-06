
package com.example.demo.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class PcmUtils {

    public static void main(String[] args) {
        byte[] bytes = convertAudioFilesToWav("/Users/yz/Downloads/wav/pcm.pcm");

        byteArrayToFile(bytes, "/Users/yz/Downloads/wav/pcm.wav");
        System.err.println(bytes);
    }

    /**
     * @param src    待转换文件路径
     * @param target 目标文件路径
     * @throws IOException 抛出异常
     */
    public static String convertAudioFilesToMp3(String src, String target) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);

        //计算长度
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();

        //填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new PcmUtils().new WaveHeader();
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;
        header.Channels = 1;
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 8000;//正常速度是8000，写成了16000，速度加快一倍
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = PCMSize;

        byte[] h = header.getHeader();

        assert h.length == 44; //WAV标准，头部应该是44字节
        //write header
        fos.write(h, 0, h.length);
        //write data stream
        fis = new FileInputStream(src);
        size = fis.read(buf);
        while (size != -1) {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();

        // PCM Convert to MP3 OK!

        return "ok";
    }

    /**
     * @param pcm 待转换文件路径
     * @throws IOException 抛出异常
     */
    public static byte[] convertAudioFilesToWav(String pcm) {
        try {
            FileInputStream fis = new FileInputStream(pcm);
            File temp = File.createTempFile("temp", "");
            FileOutputStream fos = new FileOutputStream(temp);

            //计算长度
            byte[] buf = new byte[1024 * 4];
            int size = fis.read(buf);
            int PCMSize = 0;
            while (size != -1) {
                PCMSize += size;
                size = fis.read(buf);
            }
            fis.close();

            //填入参数，比特率等等。这里用的是16位单声道 8000 hz
            WaveHeader header = new PcmUtils().new WaveHeader();
            //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
            header.fileLength = PCMSize + (44 - 8);
            header.FmtHdrLeth = 16;
            header.BitsPerSample = 16;
            header.Channels = 1;
            header.FormatTag = 0x0001;
            header.SamplesPerSec = 8000;
            header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
            header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
            header.DataHdrLeth = PCMSize;

            byte[] h = header.getHeader();

            assert h.length == 44; //WAV标准，头部应该是44字节
            //write header
            fos.write(h, 0, h.length);
            // write data stream
            fis = new FileInputStream(pcm);
            size = fis.read(buf);
            while (size != -1) {
                fos.write(buf, 0, size);
                size = fis.read(buf);
            }
            fis.close();
            fos.close();

            // Convert OK!
            return toByteArray(temp);
        } catch (IOException e) {
            return null;
        }
    }

    public class WaveHeader{
        public final char fileID[] = {'R', 'I', 'F', 'F'};
        public int fileLength;
        public char wavTag[] = {'W', 'A', 'V', 'E'};
        public char FmtHdrID[] = {'f', 'm', 't', ' '};
        public int FmtHdrLeth;
        public short FormatTag;
        public short Channels;
        public int SamplesPerSec;
        public int AvgBytesPerSec;
        public short BlockAlign;
        public short BitsPerSample;
        public char DataHdrID[] = {'d', 'a', 't', 'a'};
        public int DataHdrLeth;

        public byte[] getHeader() throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            WriteChar(bos, fileID);
            WriteInt(bos, fileLength);
            WriteChar(bos, wavTag);
            WriteChar(bos, FmtHdrID);
            WriteInt(bos, FmtHdrLeth);
            WriteShort(bos, FormatTag);
            WriteShort(bos, Channels);
            WriteInt(bos, SamplesPerSec);
            WriteInt(bos, AvgBytesPerSec);
            WriteShort(bos, BlockAlign);
            WriteShort(bos, BitsPerSample);
            WriteChar(bos, DataHdrID);
            WriteInt(bos, DataHdrLeth);
            bos.flush();
            byte[] r = bos.toByteArray();
            bos.close();
            return r;
        }

        private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
            byte[] mybyte = new byte[2];
            mybyte[1] = (byte) ((s << 16) >> 24);
            mybyte[0] = (byte) ((s << 24) >> 24);
            bos.write(mybyte);
        }

        private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
            byte[] buf = new byte[4];
            buf[3] = (byte) (n >> 24);
            buf[2] = (byte) ((n << 8) >> 24);
            buf[1] = (byte) ((n << 16) >> 24);
            buf[0] = (byte) ((n << 24) >> 24);
            bos.write(buf);
        }

        private void WriteChar(ByteArrayOutputStream bos, char[] id) {
            for (char c : id) {
                bos.write(c);
            }
        }

    }

    // MultipartFile转换为InputStream
    public static InputStream multipartToInputStream(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = null;
        File file = null;
        try {
            // 创建临时文件
            file = File.createTempFile("temp", null);
            // 把multipartFile写入临时文件
            multipartFile.transferTo(file);
            // 使用文件创建 inputStream 流
            inputStream = new FileInputStream(file);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 最后记得删除文件
            file.deleteOnExit();
            // 关闭流
            inputStream.close();
        }
        return inputStream;
    }

    public static byte[] toByteArray(File file) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] fileBytes = new byte[bis.available()];
        bis.read(fileBytes);
        bis.close();
        return fileBytes;
    }

    /**
     * byte数组转File
     * @param byteArray 字节数组
     * @param targetPath 目标路径
     */
    public static void byteArrayToFile(byte[] byteArray, String targetPath) {
        InputStream in = new ByteArrayInputStream(byteArray);
        File file = new File(targetPath);
        String path = targetPath.substring(0, targetPath.lastIndexOf("/"));
        if (!file.exists()) {
            new File(path).mkdir();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
