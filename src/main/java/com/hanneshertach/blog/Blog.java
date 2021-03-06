package com.hanneshertach.blog;

import static spark.Spark.*;

import com.hanneshertach.blog.admin.AdminController;
import com.hanneshertach.blog.media.MediaController;
import com.hanneshertach.blog.post.PostController;

import com.hanneshertach.blog.staticpage.StaticPageController;
import com.hanneshertach.blog.util.Path;
import com.hanneshertach.blog.util.ViewUtil;

public class Blog {

  public static void main(String[] args) {

    if (args.length > 0 && args[0].equals("dev")) {
      String projectDir = System.getProperty("user.dir");
      String staticDir = "/src/main/resources/public";
      staticFiles.externalLocation(projectDir + staticDir);
    } else {
      staticFiles.location("/public");
    }

    get(Path.ADMIN_ROUTE, AdminController.serveAdminPage);

    get(Path.LOGIN_ROUTE, AdminController.serveLoginPage);
    post(Path.LOGIN_ROUTE, AdminController.login);

    get(Path.LOGOUT_ROUTE, AdminController.logout);

    get(Path.CREATE_POST_ROUTE, PostController.serveCreatePostPage);
    post(Path.CREATE_POST_ROUTE, PostController.createPost);

    get(Path.EDIT_POST_ROUTE, PostController.serveEditPostPage);
    post(Path.EDIT_POST_ROUTE, PostController.editPost);

    get(Path.SINGLE_POST_ROUTE, PostController.showSinglePost);
    delete(Path.SINGLE_POST_ROUTE, PostController.deletePost);

    post(Path.MEDIA_UPLOAD_ROUTE, "multipart/form-data", MediaController.uploadMedia);
    get(Path.GET_MEDIA_ROUTE, MediaController.serveMedia);
    get(Path.MEDIA_VIEW_ROUTE, MediaController.viewAllMedia);
    delete(Path.MEDIA_DELETE_ROUTE, MediaController.deleteMediaItem);

    post(Path.PASSWORD_RESET_PATH, AdminController.changePassword);

    get(Path.ABOUT_ROUTE, StaticPageController.serveAboutPage);

    get(Path.CONTACT_ROUTE, StaticPageController.serveContactPage);

    get(Path.INDEX_ROUTE, IndexController.serveMainPage);

    get(Path.ERR_404_ROUTE, ViewUtil.render404);

    init();
  }

}
