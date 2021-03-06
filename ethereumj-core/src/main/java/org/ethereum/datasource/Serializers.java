package org.ethereum.datasource;

import org.ethereum.core.AccountState;
import org.ethereum.core.BlockHeader;
import org.ethereum.util.RLP;
import org.ethereum.util.Value;
import org.ethereum.vm.DataWord;

/**
 * Collection of common Serializers
 * Created by Anton Nashatyrev on 08.11.2016.
 */
public class Serializers {

    /**
     *  No conversion
     */
    public static class Identity<T> implements Serializer<T, T> {
        @Override
        public T serialize(T object) {
            return object;
        }
        @Override
        public T deserialize(T stream) {
            return stream;
        }
    }

    /**
     * Serializes/Deserializes AccountState instances from the State Trie (part of Ethereum spec)
     */
    public final static Serializer<AccountState, byte[]> AccountStateSerializer = new Serializer<AccountState, byte[]>() {
        @Override
        public byte[] serialize(AccountState object) {
            return object.getEncoded();
        }

        @Override
        public AccountState deserialize(byte[] stream) {
            return stream == null || stream.length == 0 ? null : new AccountState(stream);
        }
    };

    /**
     * Contract storage key serializer
     */
    public final static Serializer<DataWord, byte[]> StorageKeySerializer = new Serializer<DataWord, byte[]>() {
        @Override
        public byte[] serialize(DataWord object) {
            return object.getData();
        }

        @Override
        public DataWord deserialize(byte[] stream) {
            return new DataWord(stream);
        }
    };

    /**
     * Contract storage value serializer (part of Ethereum spec)
     */
    public final static Serializer<DataWord, byte[]> StorageValueSerializer = new Serializer<DataWord, byte[]>() {
        @Override
        public byte[] serialize(DataWord object) {
            return RLP.encodeElement(object.getNoLeadZeroesData());
        }

        @Override
        public DataWord deserialize(byte[] stream) {
            if (stream == null || stream.length == 0) return null;
            byte[] dataDecoded = RLP.decode2(stream).get(0).getRLPData();
            return new DataWord(dataDecoded);
        }
    };

    /**
     * Trie node serializer (part of Ethereum spec)
     */
    public final static Serializer<Value, byte[]> TrieNodeSerializer = new Serializer<Value, byte[]>() {
        @Override
        public byte[] serialize(Value object) {
            return object.encode();
        }

        @Override
        public Value deserialize(byte[] stream) {
            return Value.fromRlpEncoded(stream);
        }
    };

    /**
     * Trie node serializer (part of Ethereum spec)
     */
    public final static Serializer<BlockHeader, byte[]> BlockHeaderSerializer = new Serializer<BlockHeader, byte[]>() {
        @Override
        public byte[] serialize(BlockHeader object) {
            return object == null ? null : object.getEncoded();
        }

        @Override
        public BlockHeader deserialize(byte[] stream) {
            return stream == null ? null : new BlockHeader(stream);
        }
    };
}
