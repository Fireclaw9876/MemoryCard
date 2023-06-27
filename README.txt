README.txt: 10 points. A text file containing a description of the program, instructions on how to use the program, how to set up any needed files/folder/packages, and help citations. 


MemoryGame is a game that requires the user to flip different cards and match the cards with the same images. There are two different editions: Thacher Edition and Celebrity Edition. The user is able to make their choice for the editions with two buttons on the top panel of the screen. Once the user chooses an edition, they cannot switch editions during the game unless they reset the entire game. The edition changes the images that load with it. The user is also able to choose the difficulty of their game. The difficulty changes the timer length before the user is able to win or lose (2 minutes for easy, 1 minute for medium, and 30 seconds for hard). It also determines the timer length after the user flips over the card and how long it shows (0.5 seconds for easy, 0.4 seconds for medium, and 0.2 seconds for hard).


The user is also able to determine the number of cards they want to show up and the number of matches they want to make. The number of matches changes the amount of duplicate images that appear or is set up during the game. The number of cards that shows up has to be a perfect square in order to ensure that sizing and aesthetics are visually appealing. The number of image matches also has to be a factor of the number of cards so no card will be left out. Because I only manually load 8 images for the cards per each edition, I also had to make sure that the number of cards divided by the number of matches would return a number less than or equal to 8. Therefore, the number of matches must be greater than or equal to the number of cards divided by 8. The user is required to input whole numbers (no doubles, string, etc.) or else the program will crash. 


Make sure to set up the image in the correct folders. This is what each folder should contain:
CardImages
	back.png
	card1.png
	card2.png
	card3.png
	card4.png
	card5.png
	card6.png
	card7.png
	card8.png
	correctImg.png
	lostImage.png
	Moves.png
	springBackground.png
	startScreenImg.png
	timer.png
	winImage.png

CelebrityImages
        corectImage.png
        image1.png
        image2.png
        image3.png
        image4.png
        image5.png
        image6.png
        image7.png
        image8.png
	imageBack.png
                
Both the MemoryGame and MemoryCard class should be implemented as well. 


For this project, I received help from Mr. Friedman in regards to the buttons, user input screens initially, timer, and used the filler code from angry birds. For the “Welcome” pop-up, I referenced this youtube video: https://www.youtube.com/watch?v=fKF1bJ8N6J8. I also used a website (https://www.javatpoint.com/java-program-to-check-if-a-given-number-is-perfect-square) to verify that NUMCARDS is a square with the Math function, Math.floor. This returns the closest integer value so the game can verify that NUMCARDS has a whole number square root.