package com.teri.systemtracking.common.utils;

import java.io.*;

public class SerializeUtils {

      static public <T> T deserialize(byte[] bytes) throws ClassCastException {
            try {
                  try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                        return (T) in.readObject();
                  }
            } catch (IOException | ClassNotFoundException e) {
                  e.printStackTrace();
                  return null;
            }
      }

      static public byte[] serialize(Object object) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                  oos.writeObject(object);
                  oos.flush();
            } catch (IOException e) {
                  e.printStackTrace();
            }
            return baos.toByteArray();
      }
}
