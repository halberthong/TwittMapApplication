import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WebServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private OperateDB odb;
    Timer timer;

    public WebServlet() {
        super();
        odb = new OperateDB();
        odb.connect();
        TweetGet.start();
        timer = new Timer();
        setMonitor();
    }
    
    public void setMonitor() {
    	long start = 1000;
    	long interval = 1000 * 60;
    	TimerTask tsk = new TimerTask() {
    		@Override
    		public void run() {
    			int count = odb.getEntriesCount();
    			if (count > 1000) {
    				odb.autoDelete();
    			}
    		}
    	};
    	timer.scheduleAtFixedRate(tsk, start, interval);
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int minutes = -1;
		String keyword = "all";
//		Timestamp timestamp = null;
		try {
			minutes = Integer.parseInt(request.getParameter("minutes"));
			keyword = request.getParameter("keyword");			
		} catch (Exception e) {
			Common.writeLog("Incorrect data from front end", e);
		}
		List<WebData> data = odb.getDataByMinutes(minutes, keyword);
    	PrintWriter pr = response.getWriter();
    	if (data.size() == 0) {
    		pr.write("no_matching");
    	} else {
    		pr.write("start\n");
    		for(WebData ele : data) { 			
    			JSONObject obj = new JSONObject();
    			try {
    				obj.put("statusId", String.valueOf(ele.statusId));
    				obj.put("longitude", String.valueOf(ele.longitude));
    				obj.put("latitude", String.valueOf(ele.latitude));
    				obj.put("content", ele.content);
    				obj.put("keyword", ele.keyword);
    			} catch (JSONException e) {
    				Common.writeLog("JSON Error", e);
    			}
    			pr.write(obj.toString() + "\n");
        	}
    		pr.write("end");
    	}
    	pr.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
