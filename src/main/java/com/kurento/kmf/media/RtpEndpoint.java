package com.kurento.kmf.media;

import com.kurento.tool.rom.server.Param;
import com.kurento.tool.rom.server.FactoryMethod;
import java.util.List;
import com.kurento.kmf.media.events.*;

public interface RtpEndpoint extends SdpEndpoint {


	
	


    public interface Factory {

        public Builder create(@Param("mediaPipeline") MediaPipeline mediaPipeline);
    }

    public interface Builder extends AbstractBuilder<RtpEndpoint> {

        public Builder withGarbagePeriod(int garbagePeriod);
    }
}
