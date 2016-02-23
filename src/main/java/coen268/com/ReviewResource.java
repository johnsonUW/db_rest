package coen268.com;

import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("review")
public class ReviewResource {
	
	// post a new review for a restaurant
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addReview(@FormParam("restaurant_id") int restaurant_id, @FormParam("review") String reviewTxt) {
		
		Connection conn = null;
		PreparedStatement ps1 = null, ps2 = null;
		ResultSet rs = null;
		
		Review review = null;
		
		try {
			conn = DbUtil.getConnection();
			// add new review
			ps1 = conn.prepareStatement("insert into review (restaurant_id, review) values (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps1.setInt(1, restaurant_id);
			ps1.setString(2, reviewTxt);
			
			ps1.executeUpdate();
			
			// return newly add restaurant
			ResultSet generatedKeys = ps1.getGeneratedKeys();
			if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                ps2 = conn.prepareStatement("select * from review where id=?");
    			ps2.setInt(1, id);
            }
			
			rs = ps2.executeQuery();
			
			if(rs.next()) {
				review = new Review(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getTimestamp(4));
			}
			
			return Response.status(200).entity(review).build();
		}
		catch(Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		finally {
			try {
				if(rs != null) rs.close();
				if(ps1 != null) ps1.close();
				if(ps2 != null) ps2.close();
				if(conn != null) conn.close();
			}
			catch(Exception ex){}
		}
		
	}

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
				list.add(new Review(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getTimestamp(4)));
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
	public Timestamp timestamp;
	
	public Review() {}
	
	public Review(int id, int restaurant_id, String review, Timestamp timestamp) {
		this.id = id;
		this.restaurant_id = restaurant_id;
		this.review = review;
		this.timestamp = timestamp;
	}
}
