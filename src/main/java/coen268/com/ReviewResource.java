package coen268.com;

import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("review")
public class ReviewResource {

	// get all reviews for a restaurant
	@GET
	@Path("{restaurant_id: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Review> getReview(@PathParam("restaurant_id") int restaurant_id) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Review> list = new ArrayList<Review>();
		
		try {
			conn = DbUtil.getConnection();
			ps = conn.prepareStatement("select * from review where restaurant_id=?");
			ps.setInt(1, restaurant_id);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(new Review(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getDate(4)));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			}
			catch(Exception ex){}
		}
		
		return list;
	}
}

class Review {
	public int id=-1;
	public int restaurant_id=-1;
	public String review;
	public Date date;
	
	public Review() {}
	
	public Review(int id, int restaurant_id, String review, Date date) {
		this.id = id;
		this.restaurant_id = restaurant_id;
		this.review = review;
		this.date = date;
	}
}
