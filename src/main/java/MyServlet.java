import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		
		PrintWriter writer = resp.getWriter();
		
		writer.append("<html><body>");
		
		// 現在日時を取得
		Date date = new Date();
		SimpleDateFormat tokyoSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		tokyoSdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		String strDate = tokyoSdf.format(date);
		
		writer.append("<h1>現在日時：");
		writer.append(strDate);
		writer.append("</h1>");
		
		// 自身のファイルパスを取得
		final File f = new File(MyServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		
		writer.append("<h1>classファイルパス：");
		writer.append(f.getAbsolutePath() + "/" + new Object(){}.getClass().getEnclosingClass().getName());
		writer.append("</h1>");
		
		// 最終更新日時を取得
		long lastModified = f.lastModified();
		
		// 上記値からDateオブジェクトを生成
		tokyoSdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		Date utcDate = new Date(lastModified);
		strDate = tokyoSdf.format(utcDate);
		
		writer.append("<h1>classファイル最終更新日時：");
		writer.append(strDate);
		writer.append("</h1>");

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			//-----------------
			// 接続
			//-----------------
			writer.append("接続<br>");
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/sample-app-db", // "jdbc:postgresql://[場所(Domain)]:[ポート番号]/[DB名]"
			"proxyuser", // ログインロール
			"123456"); // パスワード
			statement = connection.createStatement();
			
			//-----------------
			// SQLの発行
			//-----------------
			//ユーザー情報のテーブル
			writer.append("SQLの発行<br>");
			resultSet = statement.executeQuery("SELECT * FROM t_sample");
			
			//-----------------
			// 値の取得
			//-----------------
			// フィールド一覧を取得
			writer.append("値の取得<br>");
			List<String> fields = new ArrayList<String>();
			ResultSetMetaData rsmd = resultSet.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				fields.add(rsmd.getColumnName(i));
			}
			
			//結果の出力
			int rowCount = 0;
			while (resultSet.next()) {
				rowCount++;
				writer.append("---------------------------------------------------" + "<br>");
				writer.append("--- Rows:" + rowCount + "<br>");
				writer.append("---------------------------------------------------" + "<br>");
				System.out.println("---------------------------------------------------");
				System.out.println("--- Rows:" + rowCount);
				System.out.println("---------------------------------------------------");
				
				//値は、「resultSet.getString(<フィールド名>)」で取得する。
				for (String field : fields) {
					writer.append(field + ":" + resultSet.getString(field) + "<br>");
					System.out.println(field + ":" + resultSet.getString(field));
				}
			}
		} catch (Exception e) {
			writer.append(e.getMessage() + "<br>");
			e.printStackTrace(writer);
		} finally {
			try {
				//接続を切断する
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {}
		}
		
		writer.append("</body></html>");
		writer.flush();
	}
}