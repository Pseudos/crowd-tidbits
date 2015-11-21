package server.rest.response;

import java.util.List;

import server.entity.Post;

public class PostResponse extends DefaultResponse {
    List<Post> posts;
    int numPosts;

    /**
     * @return the numPosts
     */
    public int getNumPosts() {
        return numPosts;
    }

    /**
     * @param numPosts the numPosts to set
     */
    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    /**
     * @return the post
     */
    public List<Post> getPosts() {
        return posts;
    }

    /**
     * @param post the post to set
     */
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    
}
