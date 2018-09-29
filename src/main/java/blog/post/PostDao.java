package blog.post;

import blog.database.Dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostDao implements Dao<Post> {

  private Connection conn = null;

  public PostDao() {

    try {
      Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Error fetching database driver.");
    }

    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost/blog?user=root");
    } catch (Exception e) {
      throw new RuntimeException("Error connecting to database.");
    }

  }

  @Override
  public void removeItem(Post post) {

    try (Statement stmt = conn.createStatement()) {

      PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM posts WHERE id=? AND title=?;"
      );

      ps.setObject(1, post.getId());
      ps.setObject(2, post.getTitle());

      ps.execute();

    } catch (SQLException e) {
      throw new RuntimeException("Error creating statement.");
    }

  }

  @Override
  public void addItem(Post post) {
    addPost(post.getTitle(), post.getContents());
  }

  @Override
  public List<Post> getItems() {

    List<Post> posts = new ArrayList<>();

    ResultSet rs = null;

    try (Statement stmt = conn.createStatement()) {

      rs = stmt.executeQuery("SELECT * FROM post;");

      while (rs.next()) {
        Post post = new Post(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("body"),
            rs.getTimestamp("dateAndTime").toLocalDateTime(),
            this
        );

        posts.add(post);

      }

    } catch (SQLException e) {
      throw new RuntimeException("Error trying to execute statement on database.");
    } finally {
      try {
        rs.close();
      } catch (SQLException | NullPointerException e) {

      }

    }

    return posts;
  }

  @Override
  public Post getItem(String key) {
    ResultSet rs = null;

    try (Statement stmt = conn.createStatement()) {

      PreparedStatement ps = conn.prepareStatement("SELECT * FROM post WHERE title=?;");
      ps.setObject(1, key);
      rs = ps.executeQuery();

      if (rs.next()) {
        return new Post(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("body"),
            rs.getTimestamp("dateAndTime").toLocalDateTime(),
            this
        );

      }

    } catch (SQLException e) {
      throw new RuntimeException("Error trying to execute statement on database.");
    } finally {
      try {
        rs.close();
      } catch (SQLException | NullPointerException e) {

      }

    }

    return null;

  }

  public Post getItemByID(int key) {
    ResultSet rs = null;

    try (Statement stmt = conn.createStatement()) {

      PreparedStatement ps = conn.prepareStatement("SELECT * FROM post WHERE id=?;");
      ps.setObject(1, key);
      rs = ps.executeQuery();

      if (rs.next()) {
        return new Post(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("body"),
            rs.getTimestamp("dateAndTime").toLocalDateTime(),
            this
        );

      }

    } catch (SQLException e) {
      throw new RuntimeException("Error trying to execute statement on database.");
    } finally {
      try {
        rs.close();
      } catch (SQLException | NullPointerException e) {

      }

    }

    return null;

  }

  public void addPost(String title, String contents) {

    try (Statement stmt = conn.createStatement()) {

      PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO post (title, body) VALUES (?, ?);"
      );

      ps.setObject(1, title);
      ps.setObject(2, contents);

      ps.execute();

    } catch (SQLException e) {
      throw new RuntimeException("Error trying to create statement.");
    }

  }

  public List<Post> getPostsTruncated(int len) {

    List<Post> truncatedPosts = new ArrayList<>();
    List<Post> posts = getItems();

    for (Post post : posts) {
      truncatedPosts.add(new Post(
          post.getId(),
          post.getTitle(),
          post.getContents().substring(0, Math.min(post.getContents().length(), len)) + "...",
          post.getDateObject(),
          post.getDao()
      ));
    }

    return truncatedPosts;

  }

}
