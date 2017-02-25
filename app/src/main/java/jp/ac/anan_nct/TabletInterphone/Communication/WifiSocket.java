package jp.ac.anan_nct.TabletInterphone.Communication;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import jp.ac.anan_nct.TabletInterphone.Const;
import android.util.Log;

public class WifiSocket{
	private static final int PORT = 60614; //ポート番号
	private static final int TIMEOUT_MS= 10000; //タイムアウト10秒

	private InputStream in;
	private OutputStream out;
	private ObjectInputStream oIn;
	private ObjectOutputStream oOut;
	private Socket socket;
	private ServerSocket serverSocket;


	/***************************
	 * サーバーに接続
	 *
	 * @param ipAddress 接続対象プライベートIPアドレス
	 * @return 接続成功時、trueを返す。
	 ***************************/
	public boolean connectToServer(String ipAddress){
		try{
			//ソケット生成
			socket = new Socket();

			//サーバー側に接続要求
			socket.connect(new InetSocketAddress(ipAddress, PORT), TIMEOUT_MS); //接続

			//入出力取得
			out = socket.getOutputStream();
			in = socket.getInputStream();
			oOut = new ObjectOutputStream(out);
			oIn = new ObjectInputStream(in);
		}
		catch(IOException e){
			android.util.Log.d("aaa",e.toString());
			stopConnect();
			return false;
		}

		return true;
	}


	/***************************
	 * サーバーモードで接続待機
	 *
	 * @return 接続成功時、trueを返す。
	 ***************************/
	public boolean makeServer(){
    	try {
    		//サーバー作成
    		serverSocket = new ServerSocket(PORT);

    		// クライアント側からの接続要求待ち。ソケットが返される。
    		socket = serverSocket.accept();

			//入出力取得
			out = socket.getOutputStream();
			in = socket.getInputStream();
			oOut = new ObjectOutputStream(out);
			oIn = new ObjectInputStream(in);
    	}
    	catch (IOException e) {
    		stopConnect();
    		return false;
    	}
		finally{
			close(serverSocket);
			serverSocket = null;
		}

    	return true;
	}


	/***************************
	 * 通信切断
	 ***************************/
	public void stopConnect(){
		close(oIn);
		oIn = null;

		close(oOut);
		oOut = null;

		close(in);
		in = null;

		close(out);
		out = null;

		close(socket);
		socket = null;
	}


	/***************************
	 * サーバーモード終了
	 ***************************/
	public void stopServer(){
		close(serverSocket);
		serverSocket = null;

		stopConnect();
	}


	/***************************
	 * オブジェクト送信
	 *
	 * @param object シリアライズされた送信データ。
	 * @return データ送信成功時、trueを返す。
	 ***************************/
	public boolean writeObject(final Serializable object) {
		synchronized(oOut){
			try{
				oOut.writeObject(object);
				oOut.flush();
				oOut.reset();
			}
			catch(Exception e){
				return false;
			}
			return true;
		}
	}


	/***************************
	 * オブジェクト受信
	 *
	 * @return 受信したシリアライズデータを返す。受信失敗時はnullを返す。
	 ***************************/
	public Object readObject(){
		try{
			return oIn.readObject();
		}
		catch(Exception e){
		}
		return null;
	}


	/***************************
	 * 入出力のclose処理
	 *
	 * @param socket close処理する入出力
	 ***************************/
	private synchronized void close(Socket socket){
		if(socket != null){
			try{
				socket.close();
			}
			catch(IOException e){}
		}
	}


	/***************************
	 * 入出力のclose処理
	 *
	 * @param serverSocket close処理する入出力
	 ***************************/
	private synchronized void close(ServerSocket serverSocket){
		if(serverSocket != null){
			try{
				serverSocket.close();
			}
			catch(IOException e){}
		}
	}


	/***************************
	 * 入出力のclose処理（SocketとServerSocketには使用禁止）
	 * @
	 * @param closeable close処理する入出力
	 ***************************/
	private synchronized void close(Closeable closeable){
		if(closeable != null){
			try{
				closeable.close();
			}
			catch(IOException e){}
		}
	}
	
	/////////////////////////1109追加
	public boolean socketisClosed(){
		if(serverSocket != null){
			return serverSocket.isClosed();
		}
		if(socket != null){
			return socket.isClosed();
		}
		return false;
	}
	//////////////////////////////
	
}