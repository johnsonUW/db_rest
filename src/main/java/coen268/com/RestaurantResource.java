package coen268.com;

import java.sql.*;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("restaurant")
public class RestaurantResource {
	// post a new restaurant
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addRestaurant(@FormParam("name") String name, @FormParam("address") String address) {
		Connection conn = null;
		PreparedStatement ps1 = null, ps2 = null, ps3 = null;
		ResultSet rs1 = null, rs2 = null;
		
		Restaurant restaurant = null;
		
		try {
			conn = DbUtil.getConnection();
			ps1 = conn.prepareStatement("select * from restaurant where name=? and address=?");
			ps1.setString(1, name);
			ps1.setString(2, address);
			
			rs1 = ps1.executeQuery();
			
			if(rs1.next()) {
				// restaurant already exists
				restaurant = new Restaurant(rs1.getInt(1), rs1.getString(2), rs1.getString(3));
				return Response.status(200).entity(restaurant).build();
			}
			
			// add new restaurant
			ps2 = conn.prepareStatement("insert into restaurant (name, address) values (?, ?)");
			ps2.setString(1, name);
			ps2.setString(2, address);
			
			ps2.executeUpdate();
			
			// return newly add restaurant
			ps3 = conn.prepareStatement("select * from restaurant where name=? and address=?");
			ps3.setString(1, name);
			ps3.setString(2, address);
			
			rs2 = ps3.executeQuery();
			
			if(rs2.next()) {
				restaurant = new Restaurant(rs2.getInt(1), rs2.getString(2), rs2.getString(3));
			}
			return Response.status(200).entity(restaurant).build();
		}
		catch(Exception e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
		finally {
			try {
				if(rs1 != null) rs1.close();
				if(rs2 != null) rs2.close();
				if(ps1 != null) ps1.close();
				if(ps2 != null) ps2.close();
				if(ps3 != null) ps3.close();
				if(conn != null) conn.close();
			}
			catch(Exception ex){}
		}
        
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

