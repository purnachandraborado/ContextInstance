import java.util.*;



class FogNode {
  Random rand = new Random();
  static int instanceId = 1;// count the foognode
  int id;// store id
  int cMax;// store ci maximum capacity
  int cTotal;// total number of ci present in the resepective fog node
  int cRemaining;// store remaining c
  int ciCapacity; // Check for the member if it is of use somewhere, otherwise delete it.
  int requestCounter;// count the number of elemnts present in request list
  Request[] scheduledReqList = new Request[100];
  static int[][] distanceMatrix;
  // Arrays of different application types like SV - Smart Vehicle, ST- Smart
  // Transportation, etc.
  String SHC[] = new String[100];
  String SA[] = new String[100];
  String SE[] = new String[100];
  String ST[] = new String[100];
  String SV[] = new String[100];

  FogNode() {// FogNode class constructor
    this.id = instanceId++;
    this.requestCounter = 0;
    this.cMax = rand.nextInt((17 - 10) + 1) + 10;
    for (int i = 0; i < rand.nextInt(5); i++) {
      this.SHC[i] = "C";
    }
    for (int i = 0; i < rand.nextInt(5); i++) {
      this.SA[i] = "C";
    }
    for (int i = 0; i < rand.nextInt(5); i++) {
      this.SE[i] = "C";
    }
    for (int i = 0; i < rand.nextInt(5); i++) {
      this.ST[i] = "C";
    }
    for (int i = 0; i < rand.nextInt(5); i++) {
      this.SV[i] = "C";
    }
  }

  public void createDistanceMatrix(int numberOfFogNodes)// this ,ethod is used to create the distance matrix
  {
    Random rand = new Random();
    int i, j;
    this.distanceMatrix = new int[numberOfFogNodes][numberOfFogNodes];
    for (i = 0; i < numberOfFogNodes; i++) {
      for (j = 0; j < numberOfFogNodes; j++) {
        if (this.distanceMatrix[i][j] == 0) {
          if (i == j) {
            this.distanceMatrix[i][j] = 0;
          } else {
            this.distanceMatrix[i][j] = rand.nextInt(20);
            this.distanceMatrix[j][i] = distanceMatrix[i][j];
          }
        }
      }
    }
    System.out.println();
    System.out.println("Generated distance matrix for the fog network:: ");
    for (i = 0; i < numberOfFogNodes; i++) {
      for (j = 0; j < numberOfFogNodes; j++) {
        System.out.print(this.distanceMatrix[i][j] + "\t");
      }
      System.out.println();
    }
    System.out.println();
  }

  public void addRequestList(Request r) {// this method is used to add request to the scheduled request list of fog node
    // System.out.println("Fog Node ID:: " + this.id + " , request counter:: " + this.requestCounter);
    this.scheduledReqList[this.requestCounter] = new Request();
    this.scheduledReqList[this.requestCounter].id = r.id;
    this.scheduledReqList[this.requestCounter].isScheduled = r.isScheduled;
    this.scheduledReqList[this.requestCounter].appType = r.appType;
    this.scheduledReqList[this.requestCounter].ciRequired = r.ciRequired;
    this.scheduledReqList[this.requestCounter].expServiceTime = r.expServiceTime;
    this.requestCounter++;
    // System.out.println("| Request ID | Fog Node ID |");

    System.out.println("| ReqID-" + r.id + "\t| FogNodeID-" + this.id + "\t|");
  }

  public String[] deleteNumberOfCi(String appType, int checkCiValue) {
    // deletes the number of context instances from the appType given as parameter
    // based on checkCiValue
    int i;
    String[] newAppTypeArr = new String[100];
    if (appType.equals("SHC")) {
      for (i = 0; this.SHC[i] != null && i < newAppTypeArr.length; i++) {
        newAppTypeArr[i] = this.SHC[i + checkCiValue];
      }
    } else if (appType.equals("SA")) {
      for (i = 0; this.SA[i] != null && i < newAppTypeArr.length; i++) {
        newAppTypeArr[i] = this.SA[i + checkCiValue];
      }
    } else if (appType.equals("SE")) {
      for (i = 0; this.SE[i] != null && i < newAppTypeArr.length; i++) {
        newAppTypeArr[i] = this.SE[i + checkCiValue];
      }
    } else if (appType.equals("ST")) {
      for (i = 0; this.ST[i] != null && i < newAppTypeArr.length; i++) {
        newAppTypeArr[i] = this.ST[i + checkCiValue];
      }
    } else if (appType.equals("SV")) {
      for (i = 0; this.SV[i] != null && i < newAppTypeArr.length; i++) {
        newAppTypeArr[i] = this.SV[i + checkCiValue];
      }
    }
    return newAppTypeArr;
  }

  public int getSchedLstLen() {
    // Returns the length of scheduled request list of the fog node
    int i;
    for (i = 0; this.scheduledReqList[i] != null; i++)
      ;
    return i;
  }

  public int getSize(String[] appType) {
    // Returns the size of the appType arary given as parameter
    int counter = 0;
    for (int i = 0; i < appType.length; i++)
      if (appType[i] != null)
        counter++;
    return counter;
  }

  public int updateTotalAvailableCi() {
    // updates the cTotal of fog node object
    return this.cTotal = this.getSize(this.SHC) + this.getSize(this.SA) + this.getSize(this.SE) + this.getSize(this.ST)
        + this.getSize(this.SV);
  }

  public int checkForCi(Request r) {
    // Checks for ci available and returns an integer value as follows:
    // 0 --> ciRequired by the request = the number of ci available of the appType
    // in the fog node
    // (-ve integer N) i.e., N < 0 --> N is the number of ci not available in the
    // fog node
    // (+ve integer N) i.e., N > 0 --> N is the number of ci available more than the
    // ci required by the request
    int checkForCiValue;
    if (r.appType.equals("SHC")) {
      checkForCiValue = (this.getSize(this.SHC) - r.ciRequired);
      return checkForCiValue;
    } else if (r.appType.equals("SA")) {
      checkForCiValue = (this.getSize(this.SA) - r.ciRequired);
      return checkForCiValue;
    } else if (r.appType.equals("SE")) {
      checkForCiValue = (this.getSize(this.SE) - r.ciRequired);
      return checkForCiValue;
    } else if (r.appType.equals("ST")) {
      checkForCiValue = (this.getSize(this.ST) - r.ciRequired);
      return checkForCiValue;
    } else {// if(r.appType.equals("SV")) {{}
      checkForCiValue = (this.getSize(this.SV) - r.ciRequired);
      return checkForCiValue;
    }
  }

  public String checkForScheduling(int checkCiVal) {
    // Returns a string out of ["SCHEDULE", "SHARING", "MIGRATION"] based on the
    // checkCiValue
    if (checkCiVal >= 0) {
      // if available ci is more than the number of ci required then the request can
      // be scheduled and executed directly
      return "SCHEDULE";
    } else {
      // if there is not enough ci available in the fog node then either sharing or
      // migration needs to be done
      int tempci = (-1) * checkCiVal;
      if (this.updateRemainCiCapacity() >= tempci) {
        // if the fog node is capable of receiving the remaining ci required by the
        // request then sharing can be done
        return "SHARING";
      } else {
        // if the fog node does not have the capacity to receive the remaining ci then
        // migration needs to be done
        return "MIGRATION";
      }
    }
  }

  public int updateRemainCiCapacity() {
    // Updates the remaining capacity of the fog node to receive ci
    this.cRemaining = this.cMax - this.updateTotalAvailableCi();
    return this.cRemaining;
  }

  double getCommunicationTime() {
    // Returns an expected communication time
    return rand.nextDouble() * (0.4 - 0.2) + 0.2;
  }

  int[] getSortedIndex(int id, int numberOfNodes) {
    int[] a = new int[numberOfNodes];
    int[] b = new int[numberOfNodes];
    // System.out.println("distance value"+this.distanceMatrix[0][2]);
    // System.out.println("b created for id "+id);
    int i, j;
    // for(i=0;i<numberOfNodes;i++){
    // for(j=0;j<numberOfNodes;j++){
    // System.out.print(this.distanceMatrix[i][j]+" ");
    // }
    // System.out.println();
    // }
    for (i = 0; i < numberOfNodes; i++) {
      // System.out.println("matrix stored in b");
      // System.out.println("distanace matrix value"+this.distanceMatrix[id][i]);
      a[i] = i;
      b[i] = this.distanceMatrix[id][i];
    }
    // for(i=0;i<numberOfNodes;i++){
    // System.out.println(b[i]+" ");
    // }
    int temp1 = 0, temp2 = 0;
    for (i = 0; i < numberOfNodes - 1; i++) {
      for (j = 0; j < (numberOfNodes - i - 1); j++) {
        if (b[j] > b[j + 1]) {
          // swap elements
          // System.out.println("inside check condition");
          temp1 = b[j];
          b[j] = b[j + 1];
          b[j + 1] = temp1;
          temp2 = a[j];
          a[j] = a[j + 1];
          a[j + 1] = temp2;
        }
      }
    }
    // b=null;
    return a;
  }

  void doContextSharing(FogNode[] f, int checkCiValue, String appType, double delayTolerance, double expServiceTime) {
    // This method does the sharing of context instances between fog nodes

    int getSizeAppSender, getSizeAppReceiver, nearestFNode, i, j;

    // System.out.println("Broadcasting from Fog Node ID:: " + this.id);

    int[] a = this.getSortedIndex(this.id - 1, f.length);
    // for(i=0;i<f.length;i++){
    // System.out.println(a[i]+" ");
    // }
    // System.out.print("Eror for null");
    for (j = 0; j < f.length; j++) {
      // Traversing rest of the fog nodes
      if (j + 1 == this.id)
        continue;
      else {
        i = a[j];
      }
      if (appType.equals("SHC")) {
        getSizeAppSender = f[i].getSize(f[i].SHC);
        if (getSizeAppSender == 0)
          break;
        if (getSizeAppSender == checkCiValue) {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].SHC = f[i].deleteNumberOfCi(appType, checkCiValue);
            // System.out.println(checkCiValue + " CIs of the SHC appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.SHC);
            // System.out.println(checkCiValue + " CIs of SHC is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + checkCiValue; i++)
              this.SHC[i] = "c";
          } else
            continue;
        } else {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].SHC = f[i].deleteNumberOfCi(appType, getSizeAppSender);
            // System.out.println(getSizeAppSender + " CIs of the SHC appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.SHC);
            // System.out.println(getSizeAppSender + " CIs of SHC is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + getSizeAppSender; i++) {
              this.SHC[i] = "c";
              checkCiValue = checkCiValue - getSizeAppSender;
              continue;
            }
          } else
            continue;
        }
      } else if (appType.equals("SA")) {
        getSizeAppSender = f[i].getSize(f[i].SA);
        if (getSizeAppSender == 0)
          break;
        if (getSizeAppSender == checkCiValue) {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].SA = f[i].deleteNumberOfCi(appType, checkCiValue);
            // System.out.println(checkCiValue + " CIs of the SA appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.SA);
            // System.out.println(checkCiValue + " CIs of SA is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + checkCiValue; i++)
              this.SA[i] = "c";
          } else
            continue;
        } else {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].SA = f[i].deleteNumberOfCi(appType, getSizeAppSender);
            // System.out.println(getSizeAppSender + " CIs of the SA appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.SA);
            // System.out.println(getSizeAppSender + " CIs of SA is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + getSizeAppSender; i++) {
              this.SA[i] = "c";
              checkCiValue = checkCiValue - getSizeAppSender;
              continue;
            }
          } else
            continue;
        }
      } else if (appType.equals("SE")) {
        getSizeAppSender = f[i].getSize(f[i].SE);
        if (getSizeAppSender == 0)
          break;
        if (getSizeAppSender == checkCiValue) {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].SE = f[i].deleteNumberOfCi(appType, checkCiValue);
            // System.out.println(checkCiValue + " CIs of the SE appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.SE);
            // System.out.println(checkCiValue + " CIs of SE is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + checkCiValue; i++)
              this.SE[i] = "c";
          } else
            continue;
        } else {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].SE = f[i].deleteNumberOfCi(appType, getSizeAppSender);
            // System.out.println(getSizeAppSender + " CIs of the SE appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.SE);
            // System.out.println(getSizeAppSender + " CIs of SE is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + getSizeAppSender; i++) {
              this.SE[i] = "c";
              checkCiValue = checkCiValue - getSizeAppSender;
              continue;
            }
          } else
            continue;
        }
      } else if (appType.equals("ST")) {
        getSizeAppSender = f[i].getSize(f[i].ST);
        if (getSizeAppSender == 0)
          break;
        if (getSizeAppSender == checkCiValue) {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].ST = f[i].deleteNumberOfCi(appType, checkCiValue);
            // System.out.println(checkCiValue + " CIs of the ST appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.ST);
            // System.out.println(checkCiValue + " CIs of ST is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + checkCiValue; i++)
              this.ST[i] = "c";
          } else
            continue;
        } else {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].ST = f[i].deleteNumberOfCi(appType, getSizeAppSender);
            // System.out.println(getSizeAppSender + " CIs of the ST appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.ST);
            // System.out.println(getSizeAppSender + " CIs of ST is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + getSizeAppSender; i++) {
              this.ST[i] = "c";
              checkCiValue = checkCiValue - getSizeAppSender;
              continue;
            }
          } else
            continue;
        }
      } else if (appType.equals("SV")) {
        getSizeAppSender = f[i].getSize(f[i].SV);
        if (getSizeAppSender == 0)
          break;
        if (getSizeAppSender == checkCiValue) {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].SV = f[i].deleteNumberOfCi(appType, checkCiValue);
            // System.out.println(checkCiValue + " CIs of the SV appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.SV);
            // System.out.println(checkCiValue + " CIs of SV is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + checkCiValue; i++)
              this.SV[i] = "c";
          } else
            continue;
        } else {
          if (delayTolerance >= expServiceTime + f[i].getCommunicationTime()) {
            f[i].SV = f[i].deleteNumberOfCi(appType, getSizeAppSender);
            // System.out.println(getSizeAppSender + " CIs of the SV appType are sent from fog node id:: " + f[i].id);
            getSizeAppReceiver = this.getSize(this.SV);
            // System.out.println(getSizeAppSender + " CIs of SV is received in fog node id:: " + this.id);
            for (i = getSizeAppReceiver; i <= getSizeAppReceiver + getSizeAppSender; i++) {
              this.SV[i] = "c";
              checkCiValue = checkCiValue - getSizeAppSender;
              continue;
            }
          } else
            continue;
        }
      }
    }
  }

  int doContextMigration(Request r, int indexOFRequest, FogNode[] f) {
    // This method is called for performing the context migration among the fog
    // nodes

    System.out.println("");
    System.out.println("");

    Random rand = new Random();
    int nearestFNode = 0, sizeOfApp, checkForCiValue, count = 0, i = 0, minDistance = 0, j = 0, isFailed = 0,maxRetry=0;
    String schedulingStrValue;

    System.out.println("Context Migration process initiated for Fog Node ID:: " + this.id);
    int[] a = this.getSortedIndex(this.id - 1, f.length);
    // System.out.print("======================SORTED ARRAY :: ");
    // for (j = 0; j < a.length; j++) {
    //   System.out.print(a[j] + " ");
    // }
    // System.out.println("");
    if(r.delayTolerance==0.05){
      maxRetry=3;
    }
    else if(r.delayTolerance==0.2){
      maxRetry=6;
    }
    else if(r.delayTolerance==0.5){
      maxRetry=10;
    }
    while (count != 1) {
      // Continuing and searching for fog node to migrate untill a suitable fog node
      // is not found returning a checkForSheduling value as 'SHEDULE' or 'SHARING'
      // minDistance=this.distanceMatrix[this.id-1][0];
      ++i;
      // System.out.println("==============I value:: "+i+" ===============");
      // System.out.println("Delay Tolerence value for the Request id "+r.id+" is "+r.delayTolerance);
      // System.out.println("Max retry  value for the Request id "+r.id+" is "+maxRetry);
      // if(i>=f.length){
      //   isFailed = 1;
      //   break;
      // }
      if(i>=maxRetry){
        isFailed = 1;
        break;
      }
      nearestFNode = a[i];

      System.out.println("Trying to migrate to Fog Node " + (nearestFNode+1) );
      // Avoiding the same fog node id to be selected as another fog node for
      // migration
      if (this.id == nearestFNode + 1) {
        continue;
      }

      // Getting the number of CI present in the selected fog node
      if (r.appType.equals("SHC")) {
        sizeOfApp = f[nearestFNode].getSize(f[nearestFNode].SHC);
      } else if (r.appType.equals("SA")) {
        sizeOfApp = f[nearestFNode].getSize(f[nearestFNode].SA);
      } else if (r.appType.equals("SE")) {
        sizeOfApp = f[nearestFNode].getSize(f[nearestFNode].SE);
      } else if (r.appType.equals("ST")) {
        sizeOfApp = f[nearestFNode].getSize(f[nearestFNode].ST);
      } else { // if(r.appType.equals("SV")) {
        sizeOfApp = f[nearestFNode].getSize(f[nearestFNode].SV);
      }

      // Checking if the request or the CIs can be migrated to the selected fog node.
      // So that the scheduling or sharing can be done further.
      checkForCiValue = f[nearestFNode].checkForCi(r);
      schedulingStrValue = f[nearestFNode].checkForScheduling(checkForCiValue);

      if (checkForCiValue < 0) {
        // Making the value +ve if it is -ve
        checkForCiValue *= (-1);
      }

      if (schedulingStrValue.equals("SCHEDULE")) {
        System.out.println("Scheduling ReqID " + r.id + " to Fog Node ID " + f[nearestFNode].id);
        System.out.println("---------------------------------");
        System.out.println("|  Request  ID  |  Fog Node  ID |");
        System.out.println("---------------------------------");
        f[nearestFNode].addRequestList(r);
        System.out.println("---------------------------------");

        count = 1;
        break;
      } else if (schedulingStrValue.equals("SHARING")) {
        int getSizeAppReceiver;
        // Adding request to the selected fog node
        // Sharing from sender side
        if (r.appType.equals("SHC")) {
          this.SHC = this.deleteNumberOfCi("SHC", checkForCiValue);
          // System.out.println(checkForCiValue + " CIs of SHC is shared from fog node id::  " + this.id);
        } else if (r.appType.equals("SA")) {
          this.SA = this.deleteNumberOfCi("SA", checkForCiValue);
          // System.out.println(checkForCiValue + " number of CI of SA appType fis shared from fog  nodeid :: " + this.id);
        } else if (r.appType.equals("SE")) {
          this.SE = this.deleteNumberOfCi("SE", checkForCiValue);
          // System.out.println(checkForCiValue + " number of CI of SE appType fis shared from fog  nodeid :: " + this.id);
        } else if (r.appType.equals("ST")) {
          this.ST = this.deleteNumberOfCi("ST", checkForCiValue);
          // System.out.println(checkForCiValue + " number of CI of ST appType fis shared from fog  nodeid :: " + this.id);
        } else if (r.appType.equals("SV")) {
          this.SV = this.deleteNumberOfCi("SV", checkForCiValue);
          // System.out.println(checkForCiValue + " number of CI of SV appType fis shared from fog  nodeid :: " + this.id);
        }

        // Receiving CI in receiver (or this fog node) side
        if (r.appType.equals("SHC")) {
          getSizeAppReceiver = f[nearestFNode].getSize(f[nearestFNode].SHC);
          System.out.println(checkForCiValue + " CIs of " + r.appType + " is received in Fog Node ID "+ f[nearestFNode].id);
          for (i = getSizeAppReceiver; i <= checkForCiValue; i++) {
            f[nearestFNode].SHC[i] = "c";
          }
        } else if (r.appType.equals("SA")) {
          getSizeAppReceiver = f[nearestFNode].getSize(f[nearestFNode].SA);
          System.out.println(checkForCiValue + " CIs of " + r.appType + " is received in Fog Node ID "+ f[nearestFNode].id);
          for (i = getSizeAppReceiver; i <= checkForCiValue; i++) {
            f[nearestFNode].SA[i] = "c";
          }
        } else if (r.appType.equals("SE")) {
          getSizeAppReceiver = f[nearestFNode].getSize(f[nearestFNode].SE);
          System.out.println(checkForCiValue + " CIs of " + r.appType + " is received in Fog Node ID "+ f[nearestFNode].id);
          for (i = getSizeAppReceiver; i <= checkForCiValue; i++) {
            f[nearestFNode].ST[i] = "c";
          }
        } else if (r.appType.equals("ST")) {
          getSizeAppReceiver = f[nearestFNode].getSize(f[nearestFNode].ST);
          System.out.println(checkForCiValue + " CIs of " + r.appType + " is received in Fog Node ID "+ f[nearestFNode].id);
          for (i = getSizeAppReceiver; i <= checkForCiValue; i++) {
            f[nearestFNode].ST[i] = "c";
          }
        } else if (r.appType.equals("SV")) {
          getSizeAppReceiver = f[nearestFNode].getSize(f[nearestFNode].SV);
          System.out.println(checkForCiValue + " CIs of " + r.appType + " is received in Fog Node ID "+ f[nearestFNode].id);
          for (i = getSizeAppReceiver; i <= checkForCiValue; i++) {
            f[nearestFNode].SV[i] = "c";
          }
        }
        r.isScheduled = true;
        System.out.println("---------------------------------");
        System.out.println("|  Request  ID  |  Fog Node  ID |");
        System.out.println("---------------------------------");
        f[nearestFNode].addRequestList(r);
        System.out.println("---------------------------------");

        count = 1;
        break;

      } else if (schedulingStrValue.equals("MIGRATION")) {
        f[nearestFNode].scheduledReqList[f[nearestFNode].requestCounter-1] = null;
        if((f[nearestFNode].requestCounter - 1) != 0)
          f[nearestFNode].requestCounter = f[nearestFNode].requestCounter - 1;
        System.out.println("Retrying for context migration for fog node id:: " + this.id);
        this.scheduledReqList[this.requestCounter] = null;
        if((this.requestCounter - 1) != 0)
          this.requestCounter = this.requestCounter - 1;
        count = 0;
      }

    }

    if(isFailed == 1){
      System.out.println("Req ID "+ r.id +" failed to be executed in Fog layer due to lack of resources.");
      return isFailed;
    }
    // this.scheduledReqList=MainDemo.removeElementWithIndex(this.scheduledReqList,indexOFRequest);
    return 1;
  }

}
class Request {
  String appType;// stores the appType
  int ciRequired;// stores number of si required for the following request
  static int instanceId = 1;// count the id
  int id;// store id of request
  double expServiceTime;// store expected service time
  static double delayToleranceValue[]=new double[] {0.05,0.2,0.5};//store delay tolarance values in an array format
  double delayTolerance;// store delay tolerance
  boolean isScheduled;// store boolean value true or false
  String[] Applist = { "SHC", "SA", "SE", "ST", "SV" };// applist contains tytpes of apps present
  Request() {
    // Request class constructor
    Random rand=new Random();
    // System.out.println("delay value"+this.delayToleranceValue[new Random().nextInt(delayToleranceValue.length)]);
    this.id = instanceId++;
    this.isScheduled = false;
    this.appType = this.Applist[rand.nextInt(this.Applist.length)];
    this.ciRequired = rand.nextInt((7 - 4) + 1) + 4;
    this.expServiceTime = rand.nextDouble() * (0.5 - 0.2) + 0.2;
    this.delayTolerance=delayToleranceValue[new Random().nextInt(delayToleranceValue.length)];
  }
}
public class ContextAwareScheduler {

    public static int getScheReqLiLen(Request arr[]){
      int i=0;
      for(i=0;arr[i]!=null;i++);
      return i;
    }
  // Method to remove the element from a particular index
  public static Request[] removeElementWithIndex(Request[] arr, int index) {
    if (arr == null || index < 0 || index >= arr.length) {
      // An exception or error handler for the following
      // 1. If the array is empty
      // 2. or the index is not in the range
      // return the original array
      return arr;
    }

    // Creating an array of size one less than the original array just to store the
    // elements after removing the scheduled request
    Request[] anotherArray = new Request[arr.length - 1];

    // Copying the elements except the index of the shceduled request element
    for (int i = 0, k = 0; arr[i] != null; i++) {
      // if the index is of the scheduled element index
      if (i == index) {
        continue;
      }
      // if the index is not of the scheduled element index
      anotherArray[k++] = arr[i];
    }
    // return the resultant array after copying
    return anotherArray;
  }

  public static double getServiceDelay(long start, long end, int cMax, int noOfRequests){
    return  ((end - start) + (23.15 * noOfRequests) + cMax);
  }

  public static void main(String[] args) {

    Random rand = new Random();
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter the number of fog nodes in fog infrastructure:: ");
    int noOfFogNodes = sc.nextInt();
    // System.out.print("Enter number of Requests :: ");
    // int noOfRequests = sc.nextInt();
    int noOfRequests = noOfFogNodes * (3 + rand.nextInt(1));
    double oneHopCommTime=0.02;
    long start = System.currentTimeMillis();

    System.out.println("Simulation started.....\n\n");
    System.out.println("Fog nodes getting instantiated....");

    Request[] r = new Request[noOfRequests];
    FogNode f[] = new FogNode[noOfFogNodes];

    // Creating an array of FogNode objects
    for (int i = 0; i < noOfFogNodes; i++) {
      f[i] = new FogNode();
    }
    f[0].createDistanceMatrix(noOfFogNodes);
    // Creating an array of Request objects
    for (int i = 0; i < noOfRequests; i++) {
      r[i] = new Request();
    }

    // Traversing and displaying the request details from the request objects array
    // for (int i = 0; i < noOfRequests; i++) {
    // System.out.println("Request Id:: " + " " + r[i].id);
    // System.out.println("App Type:: " + r[i].appType);
    // System.out.println("Context Instance required:: " + r[i].ciRequired);
    // System.out.println("");
    // }
    // Requests getting arrived at different fog nodes based on Poison Distribution
    System.out.println("Requests arriving at different fog nodes based on Poison Distribution (random topology).....");
    System.out.println("---------------------------------");
    System.out.println("|  Request  ID  |  Fog Node  ID |");
    System.out.println("---------------------------------");
    for (int i = 0; i < noOfRequests; i++) {
      f[rand.nextInt(noOfFogNodes)].addRequestList(r[i]);
      // System.out.println("delay value"+r[i].delayTolerance);
    }

    System.out.println("---------------------------------");


    // Traversing and displaying the details of all fog nodes and the requests
    // arrived at those fog nodes
    System.out.println("\nDisplaying details of all fog nodes in the infrastructure.....\n");
    for (int i = 0; i < noOfFogNodes; i++) {
      System.out.println("\n\n\n-----------------------------------------");
      System.out.println("|\t\tFOG NODE "+ (i+1) + "\t\t|");
      System.out.println("-----------------------------------------");
      System.out.println("| Attribute\t\t| Value\t\t|");
      System.out.println("-----------------------------------------");

      System.out.println("| Fog Node ID\t\t|" + f[i].id + "\t\t|");
      System.out.println("| cMax value\t\t|" + f[i].cMax + "\t\t|");
      System.out.println("| \t\t\t|\t\t|");
      System.out.println("| Apps and CIs\t\t|" + "\t\t|");
      // System.out.println("Size of SHC"+F[i].getSize(F[i].SHC));
      System.out.print("| SHC instances\t\t|");
      for (int j = 0; f[i].SHC[j] != null; j++) {
        System.out.print(f[i].SHC[j] + " ");
      }
      System.out.println("\t\t|");
      System.out.print("| SA instances\t\t|");
      for (int j = 0; f[i].SA[j] != null; j++) {
        System.out.print(f[i].SA[j] + " ");
      }
      System.out.println("" + "\t\t|");
      System.out.print("| SE instances\t\t|");
      for (int j = 0; f[i].SE[j] != null; j++) {
        System.out.print(f[i].SE[j] + " ");
      }
      System.out.println("" + "\t\t|");
      System.out.print("| ST instances\t\t|");
      for (int j = 0; f[i].ST[j] != null; j++) {
        System.out.print(f[i].ST[j] + " ");
      }
      System.out.println("" + "\t\t|");
      System.out.print("| SV instances\t\t|");
      for (int j = 0; f[i].SV[j] != null; j++) {
        System.out.print(f[i].SV[j] + " ");
      }
      System.out.println("" + "\t\t|");
      System.out.println("-----------------------------------------");

      System.out.println("-----------------------------------------");
      System.out.println("|    Requests Arrived at FogNode "+ (i+1)+" \t|");
      System.out.println("-----------------------------------------");
      System.out.println("| Req ID | App Type\t| Required CIs\t|");
      System.out.println("-----------------------------------------");
      for (int j = 0; f[i].scheduledReqList[j] != null; j++) {
        System.out.println("| " + f[i].scheduledReqList[j].id + "\t | " + f[i].scheduledReqList[j].appType + "\t\t| " + f[i].scheduledReqList[j].ciRequired + "\t\t|");
      }
      System.out.println("-----------------------------------------");

    }

    System.out.println("\n");

    int count = 1;
    System.out.println("Initiating Context Aware Scheduling....");
    // Looping till there is no request left to be scheduled in any fog node
    while (count != 0) {// while loop start id: w1
      // Traversing all fog nodes
      for (int i = 0; i < noOfFogNodes; i++) {
        // Traversing the scheduled request list of ith fog node
        for (int j = 0; f[i].scheduledReqList[j] != null; j++) {
          if (f[i].scheduledReqList[j].isScheduled == true) {
            // Removing requests from the list if it is already scheduled
            f[i].scheduledReqList = removeElementWithIndex(f[i].scheduledReqList, j);
          } else {
            // Trying to schedule the requests not scheduled yet
            int checkCiValue;
            checkCiValue = f[i].checkForCi(f[i].scheduledReqList[j]);

            String checkscheduleval;
            checkscheduleval = f[i].checkForScheduling(checkCiValue);

            // Checking status to schedule the arrived request and calling the corresponding
            // methods based on the status
            if (checkscheduleval.equals("SCHEDULE")) {
              // Checking if the delay for scheduling satisfies the delay tolerance of the
              // request
              System.out.println("");
              // System.out.println("Req ID: " + f[i].scheduledReqList[j].id + " scheduled to Fog node Id: " + f[i].id);
              f[i].scheduledReqList[j].isScheduled = true;
            } else if (checkscheduleval.equals("SHARING")) {
              System.out.println("");
              System.out.println(
                  "Broadcaasting and sharing for Req ID: " + f[i].scheduledReqList[j].id + " in Fog Node ID: " + f[i].id);
              int remainCiRequired;

              if (f[i].scheduledReqList[j].appType.equals("SHC")) {
                remainCiRequired = f[i].scheduledReqList[j].ciRequired - f[i].getSize(f[i].SHC);
              } else if (f[i].scheduledReqList[j].appType.equals("SA")) {
                remainCiRequired = f[i].scheduledReqList[j].ciRequired - f[i].getSize(f[i].SA);
              } else if (f[i].scheduledReqList[j].appType.equals("SE")) {
                remainCiRequired = f[i].scheduledReqList[j].ciRequired - f[i].getSize(f[i].SE);
              } else if (f[i].scheduledReqList[j].appType.equals("ST")) {
                remainCiRequired = f[i].scheduledReqList[j].ciRequired - f[i].getSize(f[i].ST);
              } else if (f[i].scheduledReqList[j].appType.equals("SV")) {
                remainCiRequired = f[i].scheduledReqList[j].ciRequired - f[i].getSize(f[i].SV);
              }
              f[i].scheduledReqList[j].isScheduled = true;
              if(oneHopCommTime+(getScheReqLiLen(f[i].scheduledReqList)*0.02)<=f[i].scheduledReqList[j].delayTolerance){
                f[i].doContextSharing(f, checkCiValue * (-1), f[i].scheduledReqList[j].appType,
                  f[i].scheduledReqList[j].delayTolerance, f[i].scheduledReqList[j].expServiceTime);
              }else{
                System.out.println("Context sharing failed for ReqID "+f[i].scheduledReqList[j].id+" due to delay intolerance. Retrying....");
              }
            } else if (checkscheduleval.equals("MIGRATION")) {
              // System.out.println("CALLED MIGRATION");
              if(oneHopCommTime+(getScheReqLiLen(f[i].scheduledReqList)*0.02)<=f[i].scheduledReqList[j].delayTolerance){
                if (1 == f[i].doContextMigration(f[i].scheduledReqList[j], j, f)){
                  f[i].scheduledReqList[j].isScheduled = true;
                  break;
                }
              }else{
                System.out.println("Context migration failed for ReqID "+f[i].scheduledReqList[j].id+" due to delay intolerance. Retrying....");
              }
            }
          }
        }
      }

      int countreq = 0;
      for (int i = 0; i < noOfFogNodes; i++) {
        countreq += f[i].getSchedLstLen();
      }

      if (countreq > 0) {
        count = 1;
      } else {
        count = 0;
      }

    } // while loop end id: w1
    
    long end = System.currentTimeMillis();
    System.out.println("Overall Service Delay:: " + getServiceDelay(start, end, 15, noOfRequests));
    // System.out.println("Overall Service Delay: "+ stopwatch.elapsed(TimeUnit.MILLISECONDS));
    System.out.println("\n\nSimulation completed....");
  }// Main method
}// Main demo class
