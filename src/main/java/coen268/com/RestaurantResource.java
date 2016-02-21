package coen268.com;

import java.sql.*;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("restaurant")
public class RestaurantResource {
	// post a new restaurant
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurant(@FormParam("name") String name, @FormParam("address") String address) {
		String output = "POST:";
        return Response.status(200).entity(output).build();
	}

	// get all restaurant
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Restaurant> getAllRestaurants() {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Restaurant> list = new ArrayList<Restaurant>();
		
		try {
			conn = DbUtil.getConnection();
			ps = conn.prepareStatement("select * from restaurant");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(new Restaurant(rs.getInt(1), rs.getString(2), rs.getString(3)));
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

	// get restaurant by name and address
	@GET
	@Path("{name}/{address}")
	@Produces(MediaType.APPLICATION_JSON)
	public Restaurant getRestaurant(@PathParam("name") String name, @PathParam("address") String address) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = DbUtil.getConnection();
			ps = conn.prepareStatement("select * from restaurant where name=? and address=?");
			ps.setString(1, name);
			ps.setString(2, address);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				return new Restaurant(rs.getInt(1), rs.getString(2), rs.getString(3));
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
		return new Restaurant();
	}
	
	// get restaurant by id
	@GET
	@Path("{id: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Restaurant getRestaurant(@PathParam("id") int id) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = DbUtil.getConnection();
			ps = conn.prepareStatement("select * from restaurant where id=?");
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				return new Restaurant(rs.getInt(1), rs.getString(2), rs.getString(3));
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
		return new Restaurant();
	}
}

class Restaurant {
	public int id=-1;
	public String name;
	public String address;
	
	public Restaurant() {}
	
	public Restaurant(int id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}
}

