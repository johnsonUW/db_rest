package coen268.com;

import java.sql.*;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("dish")
public class DishResource {
	
	// vote for a dish of a restaurant
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response vote(@FormParam("restaurant_id") int restaurant_id, @FormParam("name") String name) {	
		Connection conn = null;
		PreparedStatement ps1 = null, ps2 = null;
		ResultSet rs = null;
		
		Dish dish = null;
		
		try {
			conn = DbUtil.getConnection();
			
			
            ps2 = conn.prepareStatement("select * from dish where restaurant_id=? and name=?");
            ps2.setInt(1, restaurant_id);
			ps2.setString(2, name);
			
			rs = ps2.executeQuery();
			
			if(rs.next()) {
				// existing dish
				dish = new Dish(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4) + 1);
				
				// vote
				ps1 = conn.prepareStatement("update dish set voting=voting+1 where restaurant_id=? and name=?");
				ps1.setInt(1, restaurant_id);
				ps1.setString(2, name);
				
				ps1.executeUpdate();
			} else {
				// add and vote for a new dish
				ps1 = conn.prepareStatement("insert into dish (restaurant_id, name, voting) values (?, ?, 1)",
						Statement.RETURN_GENERATED_KEYS);
				ps1.setInt(1, restaurant_id);
				ps1.setString(2, name);
				ps1.executeUpdate();
				
				ResultSet generatedKeys = ps1.getGeneratedKeys();
				if (generatedKeys.next()) {
	                int id = generatedKeys.getInt(1);
	                dish = new Dish(id, restaurant_id, name, 1);
	            }
			}
			
			return Response.status(200).entity(dish).build();
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
	
	// get all dishes for a restaurant
	@GET
	@Path("{restaurant_id: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Dish> getDishes(@PathParam("restaurant_id") int restaurant_id) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Dish> list = new ArrayList<Dish>();
		
		try {
			conn = DbUtil.getConnection();
			ps = conn.prepareStatement("select * from dish where restaurant_id=?");
			ps.setInt(1, restaurant_id);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(new Dish(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4)));
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

class Dish {
	public int id=-1;
	public int restaurant_id=-1;
	public String name;
	public int voting;
	
	public Dish() {}
	
	public Dish(int id, int restaurant_id, String name, int voting) {
		this.id = id;
		this.restaurant_id = restaurant_id;
		this.name = name;
		this.voting = voting;
	}
}

