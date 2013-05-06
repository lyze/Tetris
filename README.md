Tetris
======

Background
----------
Tetris is played by maneuvering geometric shapes called "tetrominos" as they
fall down the "matrix." A tetromino consists of four square blocks, and the 
matrix is a playing field that is 10 blocks wide by 20 blocks high. The 
objective of the game is to manipulate these tetrominos to create a horizontal 
line of ten blocks spanning the matrix without gaps. When such a line is 
created, it disappears, and the blocks above will shift to take their new 
places. When a certain number of lines are "cleared", the game enters a new 
level. As the game progresses, each level causes the tetrominos to fall faster, 
and the game ends when the stack of tetrominos reaches the top of the playing 
field and no new tetrominos are able to enter. In this implementation, 
completing 15 levels is a player victory. 


Features
--------

* Hold button
* Queue preview
* SRS (Super Rotation System scheme) implementation (floor and wall kicks)
* Randomization per 7-piece bag (interesting implementation)
* Scoring and line clearing closely emulates <a href="http://www.tetrisfriends.com/help/tips_appendix.php#scoringchart">Tetris Marathon mode</a>
	* No distinction between Mini and non-Mini T-Spins since lame factor
	

Minor Features
--------------

* Tetromino sprites
* Elapsed time
* Lines cleared
* Used custom font file
* Game pause on focus loss
	

Controls
-------- 

| Key       | Function     |
| --------- | ------------ |
| `Z`       | Rotate left  |
| `X`       | Rotate right |
| `[LEFT]`  | Move left    |
| `[RIGHT]` | Move right   |
| `[UP]`    | Soft drop    |
| `[DOWN]`  | Hard drop    |
| `[SHIFT]` | Hold         |
| `[ESC]`   | (Un-)Pause   |


Things to Implement
-------------------
    * DAS functionality
    * Darker shade of blocks already locked down (to distinguish from active tetrominos)
    * Line clear animation
    * Name of the line clear performed
    * Sound effects
  
  
