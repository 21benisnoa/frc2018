/*
 * if (Location == "Left") {
 * 
 * if (Pathid == "11") { System.out.println("location 1, pathid 11"); if
 * (m_timer.get() > 0) { if (m_timer.get() < 4) { // moves robot forward and
 * grips cube m_ControlMethods.Forwards(); m_ControlMethods.Closeclaw();
 * m_ControlMethods.Raisearm(); } } else if (m_timer.get() > 4) { // turns robot
 * right if (m_timer.get() < 4.3) { m_ControlMethods.Stop();
 * m_ControlMethods.Stoparm(); } } else if (m_timer.get() > 4.3) { // moves
 * robot forward if (m_timer.get() < 6) { m_ControlMethods.Forwards(); } } else
 * if (m_timer.get() > 6) { // turns robot right if (m_timer.get() < 6.3) {
 * m_ControlMethods.Right(); } } else if (m_timer.get() > 6.3) { // moves robot
 * forward to switch and raises arm if (m_timer.get() < 7) {
 * m_ControlMethods.Forwards(); m_ControlMethods.Raisearm(); } } else if
 * (m_timer.get() > 7) { // stops robot at switch and opens claw if
 * (m_timer.get() < 7.5) { m_ControlMethods.Openclaw(); m_ControlMethods.Stop();
 * m_ControlMethods.Stoparm(); } } else if (m_timer.get() > 7.5) { // stops claw
 * pneumatics and puts robot in reverse if (m_timer.get() < 8) {
 * m_ControlMethods.Stopclaw(); m_ControlMethods.Backwards(); } } else if
 * (m_timer.get() > 8) { // stops robot if (m_timer.get() < 9) {
 * m_ControlMethods.Stop(); m_ControlMethods.Lowerarm(); } } else if
 * (m_timer.get() > 9) { // stops arm and grabs cube if (m_timer.get() < 9.3) {
 * m_ControlMethods.Stoparm(); m_ControlMethods.Closeclaw(); } } else if
 * (m_timer.get() > 9.3) { // puts robot in reverse and starts raising arm if
 * (m_timer.get() < 10) { m_ControlMethods.Raisearm();
 * m_ControlMethods.Backwards(); } } else if (m_timer.get() > 12) { // stops
 * robot and arm if (m_timer.get() < 12.2) { m_ControlMethods.Stop();
 * m_ControlMethods.Stoparm(); } } else if (m_timer.get() > 12.2) { // opens
 * claw if (m_timer.get() < 12.4) { m_ControlMethods.Openclaw(); } } else if
 * (m_timer.get() > 12.4) { // stops claw if (m_timer.get() < 12.6) {
 * m_ControlMethods.Stopclaw(); } } } // END PATHID 11 CODE else if (Pathid ==
 * "14") { System.out.println("location 1, pathid 14"); if (m_timer.get() > 0) {
 * if (m_timer.get() < 4) { // moves robot forward and grips cube
 * m_ControlMethods.Forwards(); m_ControlMethods.Closeclaw();
 * m_ControlMethods.Raisearm(); } } else if (m_timer.get() > 4) { // turns robot
 * right if (m_timer.get() < 4.3) { m_ControlMethods.Stop();
 * m_ControlMethods.Stoparm(); } } else if (m_timer.get() > 4.3) { // moves
 * robot forward if (m_timer.get() < 6) { m_ControlMethods.Forwards(); } } else
 * if (m_timer.get() > 6) { // turns robot right if (m_timer.get() < 6.3) {
 * m_ControlMethods.Right(); } } else if (m_timer.get() > 6.3) { // moves robot
 * forward to switch and raises arm if (m_timer.get() < 7) {
 * m_ControlMethods.Forwards(); m_ControlMethods.Raisearm(); } } else if
 * (m_timer.get() > 7) { // stops robot at switch and opens claw if
 * (m_timer.get() < 7.5) { m_ControlMethods.Openclaw(); m_ControlMethods.Stop();
 * m_ControlMethods.Stoparm(); } } else if (m_timer.get() > 7.5) { // stops claw
 * pneumatics and puts robot in reverse if (m_timer.get() < 8) {
 * m_ControlMethods.Stopclaw(); m_ControlMethods.Backwards(); } } else if
 * (m_timer.get() > 8) { // stops robot if (m_timer.get() < 9) {
 * m_ControlMethods.Stop(); m_ControlMethods.Lowerarm(); } } else if
 * (m_timer.get() > 9) { // stops arm and grabs cube if (m_timer.get() < 9.3) {
 * m_ControlMethods.Stoparm(); m_ControlMethods.Closeclaw(); } } else if
 * (m_timer.get() > 9.3) { // puts robot in reverse and starts raising arm if
 * (m_timer.get() < 10) { m_ControlMethods.Raisearm();
 * m_ControlMethods.Backwards(); } } else if (m_timer.get() > 10) { // turns
 * robot left if (m_timer.get() < 10.5) { m_ControlMethods.Left(); } } else if
 * (m_timer.get() > 10.5) { // moves robot forward if (m_timer.get() < 10.7) {
 * m_ControlMethods.Forwards(); } } else if (m_timer.get() > 12) { // stops arm
 * and turns robot right if (m_timer.get() < 12.5) { m_ControlMethods.Stoparm();
 * m_ControlMethods.Right(); } } else if (m_timer.get() > 12.5) { // moves robot
 * backwards if (m_timer.get() < 13.4) { m_ControlMethods.Backwards(); } } else
 * if (m_timer.get() > 13.4) { // opens claw if (m_timer.get() < 13.6) {
 * m_ControlMethods.Openclaw(); } } else if (m_timer.get() > 13.6) { // stops
 * claw if (m_timer.get() < 13.8) { m_ControlMethods.Stopclaw(); } } } // END
 * PATHID 14 CODE else if (Pathid == "22") {
 * System.out.println("location 1, pathid 22"); if (m_timer.get() < 4) { //
 * moves robot forward and grips cube m_ControlMethods.Forwards();
 * m_ControlMethods.Closeclaw(); m_ControlMethods.Raisearm(); } else if
 * (m_timer.get() > 4) { // turns robot right if (m_timer.get() < 4.3) {
 * m_ControlMethods.Stop(); m_ControlMethods.Stoparm(); } } else if
 * (m_timer.get() > 4.3) { // moves robot forward if (m_timer.get() < 8) {
 * m_ControlMethods.Forwards(); } } else if (m_timer.get() > 6) { // turns robot
 * right if (m_timer.get() < 8.3) { m_ControlMethods.Right(); } } else if
 * (m_timer.get() > 8.3) { // moves robot forward to switch and raises arm if
 * (m_timer.get() < 9) { m_ControlMethods.Forwards();
 * m_ControlMethods.Raisearm(); } } else if (m_timer.get() > 9) { // stops robot
 * at switch and opens claw if (m_timer.get() < 9.5) {
 * m_ControlMethods.Openclaw(); m_ControlMethods.Stop();
 * m_ControlMethods.Stoparm(); } } else if (m_timer.get() > 9.5) { // stops claw
 * pneumatics and puts robot in reverse if (m_timer.get() < 10) {
 * m_ControlMethods.Stopclaw(); m_ControlMethods.Backwards(); } } else if
 * (m_timer.get() > 10) { // stops robot if (m_timer.get() < 11) {
 * m_ControlMethods.Stop(); m_ControlMethods.Lowerarm(); } } else if
 * (m_timer.get() > 11) { // stops arm and grabs cube if (m_timer.get() < 11.3)
 * { m_ControlMethods.Stoparm(); m_ControlMethods.Closeclaw(); } } else if
 * (m_timer.get() > 11.3) { // puts robot in reverse and starts raising arm if
 * (m_timer.get() < 12) { m_ControlMethods.Raisearm();
 * m_ControlMethods.Backwards(); } } else if (m_timer.get() > 14) { // stops
 * robot and arm if (m_timer.get() < 14.2) { m_ControlMethods.Stop();
 * m_ControlMethods.Stoparm(); } } else if (m_timer.get() > 14.2) { // opens
 * claw if (m_timer.get() < 14.4) { m_ControlMethods.Openclaw(); } } else if
 * (m_timer.get() > 14.4) { // stops claw if (m_timer.get() < 14.6) {
 * m_ControlMethods.Stopclaw(); } } } // END PATHID 22 CODE else if (Pathid ==
 * "23") { System.out.println("location 1, pathid 23"); if (m_timer.get() < 4) {
 * // moves robot forward and grips cube m_ControlMethods.Forwards();
 * m_ControlMethods.Closeclaw(); m_ControlMethods.Raisearm(); } else if
 * (m_timer.get() > 4) { // turns robot right if (m_timer.get() < 4.3) {
 * m_ControlMethods.Stop(); m_ControlMethods.Stoparm(); } } else if
 * (m_timer.get() > 4.3) { // moves robot forward if (m_timer.get() < 8) {
 * m_ControlMethods.Forwards(); } } else if (m_timer.get() > 6) { // turns robot
 * right if (m_timer.get() < 8.3) { m_ControlMethods.Right(); } } else if
 * (m_timer.get() > 8.3) { // moves robot forward to switch and raises arm if
 * (m_timer.get() < 9) { m_ControlMethods.Forwards();
 * m_ControlMethods.Raisearm(); } } else if (m_timer.get() > 9) { // stops robot
 * at switch and opens claw if (m_timer.get() < 9.5) {
 * m_ControlMethods.Openclaw(); m_ControlMethods.Stop();
 * m_ControlMethods.Stoparm(); } } else if (m_timer.get() > 9.5) { // stops claw
 * pneumatics and puts robot in reverse if (m_timer.get() < 10) {
 * m_ControlMethods.Stopclaw(); m_ControlMethods.Backwards(); } } else if
 * (m_timer.get() > 10) { // stops robot if (m_timer.get() < 11) {
 * m_ControlMethods.Stop(); m_ControlMethods.Lowerarm(); } } else if
 * (m_timer.get() > 11) { // stops arm and grabs cube if (m_timer.get() < 11.3)
 * { m_ControlMethods.Stoparm(); m_ControlMethods.Closeclaw(); } } else if
 * (m_timer.get() > 11.3) { // puts robot in reverse and starts raising arm if
 * (m_timer.get() < 12) { m_ControlMethods.Raisearm();
 * m_ControlMethods.Backwards(); } } else if (m_timer.get() > 12) { // turns
 * robot left if (m_timer.get() < 12.5) { m_ControlMethods.Left(); } } else if
 * (m_timer.get() > 12.5) { // moves robot backwards if (m_timer.get() < 12.7) {
 * m_ControlMethods.Backwards(); } } else if (m_timer.get() > 14) { // stops arm
 * and turns robot right if (m_timer.get() < 14.5) { m_ControlMethods.Stoparm();
 * m_ControlMethods.Right(); } } else if (m_timer.get() > 16) { // stops robot
 * and arm if (m_timer.get() < 16.2) { m_ControlMethods.Stop();
 * m_ControlMethods.Stoparm(); } } else if (m_timer.get() > 16.2) { // opens
 * claw if (m_timer.get() < 16.4) { m_ControlMethods.Openclaw(); } } else if
 * (m_timer.get() > 16.4) { // stops claw if (m_timer.get() < 16.6) {
 * m_ControlMethods.Stopclaw(); } } } // END PATHID 23 CODE } // End LEFT
 * position code
 * 
 * if (Location == "Middle") { if (Pathid = "24") {
 * System.out.println("location 2"); if (m_timer.get() > 0) { // moves robot
 * forward and gribs cube if (m_timer.get() < 3) { m_ControlMethods.Forwards();
 * m_ControlMethods.Closeclaw(); } } else if (m_timer.get() > 3) { // turns
 * robot left if (m_timer.get() < 3.4) { m_ControlMethods.Left(); } } else if
 * (m_timer.get() > 3.4) { // moves robot forward if (m_timer.get() < 5.4) {
 * m_ControlMethods.Forwards(); } } else if (m_timer.get() > 5.4) { // turns
 * robot right if (m_timer.get() < 5.8) { m_ControlMethods.Right(); } } else if
 * (m_timer.get() > 5.8) { // moves robot forward if (m_timer.get() < 8) {
 * m_ControlMethods.Forwards(); } } else if (m_timer.get() > 8) { // turns robot
 * right if (m_timer.get() < 8.4) { m_ControlMethods.Right(); } } else if
 * (m_timer.get() > 8.4) { // moves robot forward if (m_timer.get() < 10) {
 * m_ControlMethods.Forwards(); } } else if (m_timer.get() > 10) { // turns
 * robot right to face switch if (m_timer.get() < 10.4) {
 * m_ControlMethods.Right(); } } else if (m_timer.get() > 10.4) { // moves robot
 * forward to switch and raises arm if (m_timer.get() < 11) {
 * m_ControlMethods.Forwards(); m_ControlMethods.Raisearm(); } } else if
 * (m_timer.get() > 11) { // opens claw and stops arm if (m_timer.get() < 11.2)
 * { m_ControlMethods.Openclaw(); m_ControlMethods.Stoparm(); } } else if
 * (m_timer.get() > 11.2) { // stops claw if (m_timer.get() < 11.4) {
 * m_ControlMethods.Stopclaw(); } } } // End Pathid ?? } // End MIDDLE position
 * code if (Location == "Right") { if (Pathid == "11") { // checks for pathid
 * being 11 System.out.println("location 3, pathid 11"); } else if (Pathid ==
 * "14") { // checks for pathid being 14
 * System.out.println("location 3, pathid 14"); } else if (Pathid == "22") { //
 * checks for pathid being 22 System.out.println("location 3, pathid 22"); }
 * else if (Pathid == "23") { // checks for Pathid being 23
 * System.out.println("location 3, pathid 23"); } } // End RIGHT position code
 */