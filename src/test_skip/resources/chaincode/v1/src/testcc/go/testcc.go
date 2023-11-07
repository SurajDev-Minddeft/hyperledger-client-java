package main

import (
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
	"strconv"
	"strings"
)

type TestCC struct{}

func main() {
	err := shim.Start(new(TestCC))
	if err != nil {
		fmt.Printf("Error starting TestCC chaincode: %s", err)
	}
}

func (cc *TestCC) Init(stub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

func (cc *TestCC) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, params := stub.GetFunctionAndParameters()
	ints := stringsToInt(params[0])
	if function == "ADD" {
		stringResult := strconv.Itoa(cc.Sum(ints))
		err := stub.PutState("SUM", []byte(stringResult))
		if err != nil {
			return shim.Error("failed to store sum")
		}
		return shim.Success(nil)
	} else if function == "GET_SUM" {
		state, err := stub.GetState(params[0])
		if err != nil {
			return shim.Error("failed to get sum")
		}
		return shim.Success(state)
	}
	return shim.Error(fmt.Sprintf("Unknown function %s", function))
}

func (cc *TestCC) Sum(numbers []int) int {
	result := 0
	for _, num := range numbers {
		result += num
	}
	return result
}

func stringsToInt(stringNumbers string) []int {
	var result []int
	strArray := strings.Split(stringNumbers, " ")
	for _, strNum := range strArray {
		num, _ := strconv.Atoi(strNum)
		result = append(result, num)
	}
	return result
}

