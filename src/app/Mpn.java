package app;


import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*** プレイヤークラス ***/

class Player{

    private int x;
    private int y;
    private final int WIDTH = 96;
    private final int HEIGHT = 400;

    public Player(){
        x = MainPanel.WIDTH/2;
        y = HEIGHT;
    }

    //移動
    public void move(int x_){
        x = x_;
        if(x<WIDTH/2){
            x=WIDTH/2;
        }else if(x>MainPanel.WIDTH-WIDTH/2){
            x = MainPanel.WIDTH-WIDTH/2;
        }

    }

    //表示
    public void ObjectVeiw(Graphics g,Image image){

        g.drawImage(image,x-WIDTH/2,y,null);

    }

    //落下物をとったかどうか
    public Boolean HitObject(FallingObject fallingobj){

        Rectangle playerRect = new Rectangle(
                x - WIDTH / 2,HEIGHT,WIDTH,98);

        //落下物の位置を調べる
        Point point = fallingobj.getP();
        Rectangle fellObjRect = new Rectangle(
                point.x,point.y,50,50);

        if (playerRect.intersects(fellObjRect)) {
            return true;
        }


        return false;
    }

}

/*** 落下物クラス ***/

class FallingObject{

    //スピード
    private int speed;
    //位置
    private int x;
    private int y;
    private final int score;
    private Image image;

    //初期設定
    public FallingObject(int _speed,int _score){
        speed = _speed;
        score = _score;
        //保管庫いき
        x = -100;
        y = -100;
    }

    //落下移動
    public void FallingMove(){
        y+=speed;
    }

    //落下物を表示
    public void ObjectVeiw(Graphics g,String imgname){

        ImageIcon icon =
            new ImageIcon(getClass().getClassLoader().getResource(imgname));
        image = icon.getImage();

        g.drawImage(image,x,y,null);

    }

    //得点を返す
    public int getScore(){
        return score;
    }

    //フィールドからでていれば保管庫
    public boolean getExistence(){
        if(y>MainPanel.HEIGHT){
            x=-100;
            y=-100;
            return true;
        }

        return false;
    }

    //初期位置を決める
    public void Potison(int n){

        int r = (int)(50+Math.random()*(MainPanel.WIDTH-100));
        int h;

        h = (n+1)*-100;

        //位置の更新
        x = r;
        y = h;

    }

    //保管庫にあるか調べる
    public Boolean Space(){

        if(x==-100 && y==-100){
            return true;
        }else{
            return false;
        }

    }

    //位置を返す
    public Point getP(){

        Point point = new Point(x,y);

        return point;
    }

    //保管庫にいれる
    public void setSpace(){

        x=-100;
        y=-100;

    }

    //スピードを変える
    public void speedchang(int speed_){

        speed = speed_;

    }

}

/*** 得点管理クラス ***/

class Score{

    private int fieldscore;
    private final int Rife = 5;
    private int rifepoint;

    //スコアの初期化
    public Score(){
        fieldscore = 0;
        rifepoint=25;
    }

    //スコアの加算
    public void setScore(int score){
        fieldscore+=score;
    }

    //現在のスコアを返す
    public int getScore(){
        return fieldscore;
    }

    //ライフを減らす
    public void setRife(){
        rifepoint-=Rife;
    }

    //ライフを返す
    public int getRife(){
        return rifepoint;
    }

    //ファイルに記録
    public void setFileData(int score,int life,String name,String pass){
        String data = "score="+score+"&life="+life+"&name="+name+"&pass="+pass+"";
        URL cgiURL=null;
        try {
            //URLの生成
            try{
                cgiURL = new URL("");
            }catch(MalformedURLException e){
                e.printStackTrace();
            }

            //設定
            HttpURLConnection uc = (HttpURLConnection)cgiURL.openConnection();
            uc.setDoOutput(true);
            uc.setUseCaches(false);

            uc.getHeaderFields();

            // CGIへの書き込み用ストリームを開く
            OutputStreamWriter pw = new OutputStreamWriter( uc.getOutputStream() );
            // CGIにデータを送信する
            pw.write(data);
            // ストリームを閉じる
            pw.flush();
            pw.close();

        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }
}


/*** 時間管理クラス ***/

class Time{

    long fastInMillis;
    //時間設定
    private final int s1Time = 150;
    private final int s2Time = 350;
    private final int s3Time = 450;
    private final int s4Time = 550;
    private final int s5Time = 700;
    private final int s6Time = 900;
    private final int s7Time = 1000;
    private final int s8Time = 2000;

    public Time(){
        fastInMillis = System.currentTimeMillis();
    }

    //秒数を返す
    public int getTime(){
        long nowInMillis = System.currentTimeMillis();
        int nowtime = ((int)nowInMillis-(int)fastInMillis)/100;
    return nowtime;
    }

    //一定の時間でスピードを変える
    public void TimeSpeed(FallingObject fillingObj){

        if(this.getTime()>s1Time){
            fillingObj.speedchang(4);
        }
        if(this.getTime()>s2Time){
            fillingObj.speedchang(5);
        }

        if(this.getTime()>s3Time){
            fillingObj.speedchang(7);
        }

        if(this.getTime()>s4Time){
            fillingObj.speedchang(8);
        }

        if(this.getTime()>s5Time){
            fillingObj.speedchang(15);
        }

        if(this.getTime()>s6Time){
            fillingObj.speedchang(20);
        }

        if(this.getTime()>s7Time){
            fillingObj.speedchang(30);
        }

        if(this.getTime()>s8Time){
            fillingObj.speedchang(50);
        }

    }

}

/*****************************
 * KEY管理クラス
 *****************************/
class Key extends KeyAdapter {
    static boolean left, right, up, down, space, enter;
    // キーが押されたときの処理
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: left = true; break;  // ←
            case KeyEvent.VK_RIGHT: right = true; break;    // →
            case KeyEvent.VK_UP: up = true; break;      // ↑
            case KeyEvent.VK_DOWN: down = true; break;  // ↓
            case KeyEvent.VK_SPACE: space = true; break;    // SPACE
            case KeyEvent.VK_ENTER: enter = true; break;    // Enter
        }
    }
    // キーが離されたときの処理
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: left = false; break; // ←
            case KeyEvent.VK_RIGHT: right = false; break;   // →
            case KeyEvent.VK_UP: up = false; break;     // ↑
            case KeyEvent.VK_DOWN: down = false; break; // ↓
            case KeyEvent.VK_SPACE: space = false; break;   // SPACE
            case KeyEvent.VK_ENTER: enter = false; break;   // Enter
        }
    }
}

/*****************************
 * パネルクラス
 *****************************/
class MainPanel extends JPanel implements Runnable,MouseMotionListener{

    static final int WIDTH = 500;
    static final int HEIGHT = 500;
    private final int SCENE_INIT = 0;   // シーン：初期化
    private final int SCENE_TITLE = 1;  // シーン：タイトル
    private final int SCENE_MAIN = 2;   // シーン：メイン
    private final int SCENE_OVER = 3;   // シーン：ゲームオーバー
    private int scene;  //シーン値格納
    Image bg;   //背景
    ImageIcon ibg;
    Image image;
    private Image offImage;     // 仮想画面
    private Graphics gv;        // 仮想画面Graphicsオブジェクト
    private Player player; //プレイヤー
    private Score score;
    private Time time;
    private FallingObject fallingObj[] = new FallingObject[5];
    private AudioClip pong;
    private AudioClip pong2;
    Thread thread;  //スレッド用
    int flag=0;

    public MainPanel(){


        //システムの初期化
        init();

    }



    //ゲームスレッド
    public void run(){

        while(thread == Thread.currentThread()){

            if(offImage==null){
                render();
            }else{

                //シーン処理
                switch (scene) {
                    case SCENE_INIT: gameInit(); break;
                    case SCENE_TITLE: gameTitle(); break;
                    case SCENE_MAIN: gameMain(); break;
                    case SCENE_OVER: gameOver(); break;
                }

            }

            // 描画
            paintScreen();

            // 20ミリ秒待つ
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }

        }

    }

    //システム初期化
    public void init(){

        //カーソルを消す
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(), "");

        setCursor(cursor);
        //キー受付
        setFocusable(true);
        addKeyListener(new Key());
        //マウスの動きを感知させる
        addMouseMotionListener(this);
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("shana.png"));
        image = icon.getImage();
        pong = Applet.newAudioClip(getClass().getClassLoader().getResource("hoi.wav"));
        pong2 = Applet.newAudioClip(getClass().getClassLoader().getResource("doeu.wav"));

        //スレッド生成
        thread = new Thread(this);
        thread.start();

        scene = SCENE_INIT;
    }

    //仮想イメージの作成
    public void render(){
        offImage = createImage(WIDTH, HEIGHT);
        if(offImage!=null){
            gv = offImage.getGraphics();
        }
    }

    //バッファを画面に転送
    private void paintScreen() {
         try {
             Graphics g = getGraphics(); // グラフィックオブジェクトを取得
             if ((g != null) && (offImage != null)) {
                 g.drawImage(offImage, 0, 0, null); // バッファイメージを画面に描画
             }
             Toolkit.getDefaultToolkit().sync();
             if (g != null) {
                 g.dispose(); // グラフィックオブジェクトを破棄
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

    //ゲーム初期化処理
    private void gameInit() {
        read();
        scene = SCENE_TITLE;

    }

    //ゲーム準備処理
    private void read(){
        player = new Player();
        score = new Score();
        time = new Time();
        for(int i=0;i<5;i++){
            fallingObj[i] = new FallingObject(3,5);
            fallingObj[i].Potison(i);
        }
        //背景
        ibg = new ImageIcon(getClass().getClassLoader().getResource("bg.png"));
        gv.drawImage(ibg.getImage(),0,0,null);
    }

    //ゲームタイトル
    private void gameTitle() {
        // タイトル画面描画
        gv.setFont(new Font("MSゴシック,", Font.BOLD, 20));
        gv.setColor(Color.red);
        gv.drawString("食べちゃえメロンパンGOGO！", 100, 250);
        gv.drawString("エンターを押せばはじまるよ！", 100, 300);
        if (Key.enter) {
            gv.clearRect(0, 0,WIDTH, HEIGHT);
            System.gc();
            // ゲームメインへ移行
            scene = SCENE_MAIN;
        }
    }

    //ゲームメイン
    private void gameMain() {
        for(int i=0;i<5;i++){
            fallingObj[i].FallingMove();
            if(fallingObj[i].getExistence()){
                score.setRife();
            }

            if(score.getRife()<=0){
                scene = SCENE_OVER;
            }

            //落下物に当たっているか調べる
            if(player.HitObject(fallingObj[i])){
                score.setScore(fallingObj[i].getScore());
                pong.play();
                fallingObj[i].setSpace();
                flag=1;
            }

            //保管庫にあるか調べる
            if(fallingObj[i].Space()){
                if(flag==0){
                    pong2.play();
                }else{
                    flag=0;
                }
                //あれば再配置
                fallingObj[i].Potison(i);
            }

             time.TimeSpeed(fallingObj[i]);
        }
        gv.clearRect(0, 0,WIDTH, HEIGHT);
        gv.drawImage(ibg.getImage(),0,0,null);
        for(int i=0;i<5;i++){
            fallingObj[i].ObjectVeiw(gv,"meron.png");
        }

        //スコアの書き込み
        gv.setColor(Color.red);
        int s = score.getScore();
        int l = score.getRife();
        gv.drawString("スコア:"+s, 10, 17);
        gv.drawString("ライフ:"+l, 150, 17);
        //時間
        gv.drawString("経過時間:"+time.getTime(), 300, 17);
        player.ObjectVeiw(gv,image);

    }

    //ゲームオーバー
    private void gameOver() {
        gv.drawString("ゲームオーバー", 180, 250);
        String user = JOptionPane.showInputDialog("ゲームオーバー","ユーザー名");
        String pass = JOptionPane.showInputDialog("パスワード");
        //スコアの記録
        if(user!=null && pass!=null && user.length()!=0 && pass.length()!=0){
            //score.setFileData(score.getScore(),score.getRife(),user,pass);
        }
        // タイトル画面へ移行
        gv.clearRect(0, 0,WIDTH, HEIGHT);
        // ゲーム準備処理を行い
        read();
        scene = SCENE_TITLE;

    }

    // マウスを動かしたとき呼び出される
    public void mouseMoved(MouseEvent e) {
        int x = e.getX(); // マウスのX座標
        if(scene==2){
            player.move(x);
        }
    }

    // マウスをドラッグしたとき呼び出される
    public void mouseDragged(MouseEvent e) {

    }

}
public class Mpn extends JApplet  {



    public void init(){

        MainPanel panel = new MainPanel();
        panel.setSize(500, 500);
        Container contena = getContentPane();
        contena.add(panel);

   }

}

