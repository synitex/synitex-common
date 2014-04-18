package synitex.common.server.map;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import java.util.List;

public interface IEntity2SoyMapper {

    SoyMapData map(Object object);

    <T extends Object> SoyListData mapList(List<T> objects);

}
