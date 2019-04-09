import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Create by tachai on 2019-04-09 09:28
 * gitHub https://github.com/TACHAI
 * Email tc1206966083@gmail.com
 */
public class ImageDraw {

    // 已经读过的路径
    public static java.util.List<String> AlreadyPath;


    //请求总数
//    public static int clientTotal = 8;

    //同时并发执行的线程数
    public static int threadTotal = 10;

    public static int count = 0;


    public static void main(String[] args)throws IOException {

        java.util.List<PathBean> list = new ArrayList<>();
        PathBean bean = new PathBean();
        bean.setCsvPath("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-1-1_clean.csv");
        bean.setReadPath("/Users/mac/Desktop/pyCharm/movieToImage/image2/1-1-1");
        bean.setOutPath("/Volumes/mac盘/image/1-1-1");
        list.add(bean);

        PathBean bean1 = new PathBean();
        bean1.setCsvPath("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-1-2_clean.csv");
        bean1.setReadPath("/Users/mac/Desktop/pyCharm/movieToImage/image2/1-1-2");
        bean1.setOutPath("/Volumes/mac盘/image/1-1-2");
        list.add(bean1);

        PathBean bean2 = new PathBean();
        bean2.setCsvPath("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-1-3_clean.csv");
        bean2.setReadPath("/Users/mac/Desktop/pyCharm/movieToImage/image2/1-1-3");
        bean2.setOutPath("/Volumes/mac盘/image/1-1-3");
        list.add(bean2);

        PathBean bean3 = new PathBean();
        bean3.setCsvPath("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-2-1_clean.csv");
        bean3.setReadPath("/Users/mac/Desktop/pyCharm/movieToImage/image2/1-2-1");
        bean3.setOutPath("/Volumes/mac盘/image/1-2-1");
        list.add(bean3);

        PathBean bean4 = new PathBean();
        bean4.setCsvPath("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-2-2_clean.csv");
        bean4.setReadPath("/Users/mac/Desktop/pyCharm/movieToImage/image2/1-2-2");
        bean4.setOutPath("/Volumes/mac盘/image/1-2-2");
        list.add(bean4);

        PathBean bean5 = new PathBean();
        bean5.setCsvPath("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-2-3_clean.csv");
        bean5.setReadPath("/Users/mac/Desktop/pyCharm/movieToImage/image2/1-2-3");
        bean5.setOutPath("/Volumes/mac盘/image/1-2-3");
        list.add(bean5);

        PathBean bean6 = new PathBean();
        bean6.setCsvPath("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-3-1_clean.csv");
        bean6.setReadPath("/Users/mac/Desktop/pyCharm/movieToImage/image2/1-3-1");
        bean6.setOutPath("/Volumes/mac盘/image/1-3-1");
        list.add(bean6);

        PathBean bean7 = new PathBean();
        bean7.setCsvPath("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-3-2_clean.csv");
        bean7.setReadPath("/Users/mac/Desktop/pyCharm/movieToImage/image2/1-3-2");
        bean7.setOutPath("/Volumes/mac盘/image/1-3-2");
        list.add(bean7);

        PathBean bean8 = new PathBean();
        bean8.setCsvPath("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-3-3_clean.csv");
        bean8.setReadPath("/Users/mac/Desktop/pyCharm/movieToImage/image2/1-3-3");
        bean8.setOutPath("/Volumes/mac盘/image/1-3-3");
        list.add(bean8);

        ExecutorService excutorService = Executors.newCachedThreadPool();
        //信号量，当前线程最大并发量
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(list.size());
        for(int i=0;i<list.size();i++){
            PathBean pathBean = list.get(i);
//            System.out.println(pathBean.getCsvPath()+"++++"+pathBean.getReadPath()+"++++"+pathBean.getOutPath());

            excutorService.execute(()->{
                try {
                    semaphore.acquire();
                    System.out.println(pathBean.getCsvPath()+"++++"+pathBean.getReadPath()+"++++"+pathBean.getOutPath());
                    draw(pathBean.getCsvPath(),pathBean.getReadPath(),pathBean.getOutPath());
                    semaphore.release();
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            countDownLatch.await();
            excutorService.shutdown();
            System.out.println("count:"+count);
//            log.info("count:{}",count);
        }catch (Exception e){
            System.out.println(e.getMessage());

//            log.error(e.getMessage());
        }



    }

    /**
     * @param csv csv的包含文件名的完整路径
     * @param read 图片读的路径
     * @param out 图片写的路径
     */
    // 画图组合起来
    private static void draw(String csv,String read,String out){
        java.util.List<String> list = readCsv(csv);


        for(int i=1;i<list.size();i++){
            String line = list.get(i);
            String[] arr = line.split(",");

//            String readPath ="/Users/mac/Desktop/pyCharm/movieToImage/image2/1-1-1";
//            String outPath = "/Volumes/mac盘/image/1-1-1";
            int a = Integer.parseInt(arr[0]);
            int b = Integer.parseInt(arr[1]);
            double c = new Double(arr[2]);
            double d = new Double(arr[3]);
            double e = new Double(arr[4]);
            double f = new Double(arr[5]);
            draw(read,out,a-3,b,c,d,e,f);
        }

    }

    /**
     * @param fileName 包含csv文件名的完整路径
     * @return  返回包含内容的数组
     */
    // 读csv文件
    private static java.util.List<String> readCsv(String fileName){
        // CSV文件路径
//        File csv = new File("/Users/mac/Desktop/pyCharm/movieToImage/csv/1-1-1_clean.csv");
        File csv = new File(fileName);
        BufferedReader br = null;
        java.util.List<String> allString = new ArrayList<>();

        try
        {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        try {
            while ((line = br.readLine()) != null)  //读取到的内容给line变量
            {
                everyLine = line;
//                System.out.println(everyLine);
                allString.add(everyLine);
            }
            System.out.println("csv表格中所有行数："+allString.size());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return allString;
    }


    /**
     * 画框
     * @param readPath  图片读的路径
     * @param outPath   图片写的路径
     * @param a 图片的序号或者说是时间  可以调节一下
     * @param b 图片中框的序号 偶数是蓝色  奇数是黄色
     * @param c x轴
     * @param d y轴
     * @param e 长
     * @param f 宽
     */
    private static void draw(String readPath,String outPath,int a,int b,double c,double d,double e,double f){
        try {
            //图片路径
//            String path = readPath;
            String realPath = readPath+"/"+a+".jpg";
            System.out.println("realPath:"+realPath);
            if(AlreadyPath==null){
                AlreadyPath = new ArrayList<>();
            }
            if(AlreadyPath.contains(realPath)){
                realPath = outPath+"/"+a+".jpg";
            }else {
                AlreadyPath.add(realPath);
            }
//            "/Users/mac/Desktop/pyCharm/movieToImage/image2/1-1-2视频U00C0T20180817193636282/597.jpg"
            BufferedImage image = ImageIO.read(new File(realPath));
            Graphics g = image.getGraphics();
            Graphics2D g2 = (Graphics2D) g;
            //画笔颜色
            if(b%2==0){
                g2.setColor(Color.blue);
            }else {
                g2.setColor(Color.yellow);
            }
            //画笔的粗细
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));


            // 因为没画框的图是画框图的3倍
            g2.drawRect(new Double(c*3).intValue(), new Double(d*3).intValue(), new Double(e*3).intValue(), new Double(f*3).intValue());//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
//            String str = b+" ("+new Double(c*3).intValue()+","+new Double(d*3).intValue()+")";



            // 设置字体大小
            g2.setFont(new Font("宋体",Font.BOLD,25));
//            g2.drawString(str,new Double(c*3).intValue(), new Double(d*3-3).intValue());
            g2.drawString(b+"",new Double(c*3+e*1.5).intValue(), new Double(d*3+f*1.5).intValue());

//            FileOutputStream out = new FileOutputStream("/Volumes/mac盘/image//600-2-java.jpg");
            //输出图片的地址
            FileOutputStream out = new FileOutputStream(outPath+"/"+a+".jpg");//输出图片的地址
            ImageIO.write(image, "jpeg", out);
        }catch (IOException error){
            System.out.println(error.getMessage());
        }
    }
}
