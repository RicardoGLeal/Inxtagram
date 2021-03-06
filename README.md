# Project 4 - *Inxtagram*

**Inxtagram** is a photo sharing app using Parse as its backend.

Time spent: **15** hours spent in total

## User Stories

The following **required** functionality is completed:

- [X] User sees app icon in home screen.
- [X] User can sign up to create a new account using Parse authentication
- [X] User can log in to his or her account
- [X] The current signed in user is persisted across app restarts
- [X] User can log out of his or her account
- [X] User can take a photo, add a caption, and post it to "Instagram"
- [X] User can view the last 20 posts submitted to "Instagram"
- [X] User can pull to refresh the last 20 posts submitted to "Instagram"
- [X] User can tap a post to view post details, including timestamp and caption.
- [X] User sees app icon in home screen

The following **stretch** features are implemented:

- [X] Style the login page to look like the real Instagram login page.
- [X] Style the feed to look like the real Instagram feed.
- [ ] User can load more posts once he or she reaches the bottom of the feed using endless scrolling.
- [X] User should switch between different tabs using fragments and a Bottom Navigation View.
  - [X] Feed Tab (to view all posts from all users)
  - [X] Capture Tab (to make a new post using the Camera and Photo Gallery)
  - [X] Profile Tab (to view only the current user's posts, in a grid)
- [X] Show the username and creation time for each post
- User Profiles:
  - [X] Allow the logged in user to add a profile photo
  - [X] Display the profile photo with each post
  - [X] Tapping on a post's username or profile photo goes to that user's profile page
  - [X] User Profile shows posts in a grid
- [X] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [X] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [ ] List anything else that you can get done to improve the app functionality!

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. Fragments, I want to know more how they work, and their lifecycle.
2. The Cellphone Camera SDK, I would like to see how can I also have access to the photos of the cell phone.
## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='capture.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [Kap](https://getkap.co/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes

Describe any challenges encountered while building the app.
- To see the details of the posts I wanted to make use of the fragments, however at first I did not know how to call the fragment from an adapter, so I asked a TA who helped me to obtain the Activity through the adapter, and with that I was able to call the fragment through the fragmentManager.
- Sometimes when I was pressing the button to go back, the application was returning me to the LoginActivity, so what I did was: override the onBack method of the MainActivity, making that it only opened the initial fragment.
- I think the most valuable thing about this app was learning to use the Parse database, integrate the cell phone camera, and make use of fragments.

## License

    Copyright [2021] [Ricardo Gonzalez Leal]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
