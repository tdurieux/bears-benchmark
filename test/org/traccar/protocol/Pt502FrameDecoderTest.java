package org.traccar.protocol;

import org.junit.Assert;
import org.junit.Test;
import org.traccar.ProtocolTest;

import java.nio.ByteOrder;

public class Pt502FrameDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        Pt502FrameDecoder decoder = new Pt502FrameDecoder();

        Assert.assertEquals(
                binary("244655533836353332383032363234333836342c3531302d56312e31322c4131312d56332e30"),
                decoder.decode(null, null, binary(ByteOrder.LITTLE_ENDIAN, "bffb192d00244655533836353332383032363234333836342c3531302d56312e31322c4131312d56332e300d0d")));

        Assert.assertEquals(
                binary("24504f532c3836353332383032363234333836342c3134343733352e3030302c412c313333322e373038332c4e2c3230342e363833312c452c302e302c3233302e30302c3032303531372c2c2c412f30303030302c31302f312c302f3233342f2f4646392f"),
                decoder.decode(null, null, binary(ByteOrder.LITTLE_ENDIAN, "24504f532c3836353332383032363234333836342c3134343733352e3030302c412c313333322e373038332c4e2c3230342e363833312c452c302e302c3233302e30302c3032303531372c2c2c412f30303030302c31302f312c302f3233342f2f4646392f0d0a")));

        Assert.assertEquals(
                binary("24504f532c3335333435313030303136342c3038323430352e3030302c412c313235342e383530312c4e2c31303035312e363735322c452c302e30302c3233372e39392c3136303531332c2c2c412f303030302c302f302f35353030302f2f6137312f"),
                decoder.decode(null, null, binary(ByteOrder.LITTLE_ENDIAN, "24504f532c3335333435313030303136342c3038323430352e3030302c412c313235342e383530312c4e2c31303035312e363735322c452c302e30302c3233372e39392c3136303531332c2c2c412f303030302c302f302f35353030302f2f6137312f0d0a")));

        Assert.assertEquals(
                binary("24504f532c3335333435313030303136342c3038323430352e3030302c412c313235342e383530312c4e2c31303035312e363735322c452c302e30302c3233372e39392c3136303531332c2c2c412f303030302c302f302f35353030302f2f6137312f"),
                decoder.decode(null, null, binary(ByteOrder.LITTLE_ENDIAN, "bffb1b6a0024504f532c3335333435313030303136342c3038323430352e3030302c412c313235342e383530312c4e2c31303035312e363735322c452c302e30302c3233372e39392c3136303531332c2c2c412f303030302c302f302f35353030302f2f6137312f0d0a")));

    }

}
