# BlogProject
### Demo deployed at AWS:
http://18.119.7.149/

------------


### Featrues for admins
- Perform CRUD operations on blogs, tags, and types.
- Save the blog as a draft.
- Recommend blogs so that they can appear in a section on the homepage.
- Turn off the comment section under certain blogs.
- Disable copyright notification on specific blogs that have reproduced or referenced content.
- has a special avatar and tags beside the comments.

------------


### Features for viewers(guests)
- View all published (not-draft) blogs.
- Search blogs by title or content.
- Group blogs by types, tags, or post years.
- Comment under each blog post or reply to other comments.

------------


### Build by Maven
Clone and use Maven to build the project, then you can run it locally.

------------

### Admin and Log in
For admin management pages, attach "/admin" at the end of URL to log in.

#### Password
**Please keep in mind that the password stored in the MySQL database is an MD5 value. Please use the MD5Util class (included in the project) to get a MD5 value for your password and store that MD5 value in the database.**

#### Admin comment
Comment as admin while logging in on the other tab.
