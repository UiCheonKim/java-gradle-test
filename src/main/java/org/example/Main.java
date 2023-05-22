package org.example;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
       find();
    }
    public static void save() throws IOException {

        final String URL = "http://123.456.78.910:8545";
        final String FROM_ADDRESS = "0xB2D9AC9eCf5bd6CF8b8AC56c709bd26830B03C3b";
        final String CONTRACT_ADDRESS = "0x04bc75be8C8E6DB9A7a418cc9c87ff659f22235E";
        final Web3j web3j =  Web3j.build(new HttpService(URL));
        final String contractMethodName = "issue_request";
        BigInteger nonce;

        List<Type> inputParams = new ArrayList<>();

        inputParams.add(new Utf8String("identityy"));
        inputParams.add(new Utf8String("issue_request"));
        inputParams.add(new Utf8String("reissue"));
        inputParams.add(new Uint256(1680084047));

        Function function = new Function(
                contractMethodName,
                inputParams,
                Collections.emptyList()
        );

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                FROM_ADDRESS, DefaultBlockParameterName.LATEST).send();

        nonce = ethGetTransactionCount.getTransactionCount();

        Transaction transaction = Transaction.createFunctionCallTransaction(
                FROM_ADDRESS,
                nonce,
                BigInteger.valueOf(1000000000),
                BigInteger.valueOf(3000000),
                CONTRACT_ADDRESS,
                FunctionEncoder.encode(function)
        );

        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();

        String transactionHash = ethSendTransaction.getTransactionHash();

        System.out.println("transactionHash = " + transactionHash);
    }

    public static void find() throws IOException {

        final String URL = "http://218.236.45.178:8545";
        final String FROM_ADDRESS = "0xB2D9AC9eCf5bd6CF8b8AC56c709bd26830B03C3b";
        final String CONTRACT_ADDRESS = "0x04bc75be8C8E6DB9A7a418cc9c87ff659f22235E";
        final Web3j web3j =  Web3j.build(new HttpService(URL));

        List<Type> inputParams = new ArrayList<>();
        inputParams.add(new Utf8String("identityy"));

        // Get Function 인스턴스 생성
        Function function2 = new Function(
                "get_data_from_identity",
                inputParams,
                Arrays.asList(new TypeReference<>(){})
        );

        String encodedFunction = FunctionEncoder.encode(function2);

        String value;
        try {
            value = web3j.ethCall(
                    Transaction.createEthCallTransaction(
                            FROM_ADDRESS,
                            CONTRACT_ADDRESS,
                            encodedFunction
                    ),
                    DefaultBlockParameterName.LATEST
            ).send().getValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(value);
    }
}