package de.oetting.wwp.controller;

import com.github.marcioos.bggclient.BGG;
import com.github.marcioos.bggclient.common.ThingType;
import com.github.marcioos.bggclient.fetch.FetchException;
import com.github.marcioos.bggclient.fetch.domain.FetchItem;
import com.github.marcioos.bggclient.search.SearchException;
import com.github.marcioos.bggclient.search.domain.SearchItem;
import com.github.marcioos.bggclient.search.domain.SearchOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "api/bgg")
public class BGGController {

    @GetMapping(path = "/search/{searchTerm}")
    public List<SearchItem> searchBgg(@PathVariable("searchTerm") String searchTerm) throws SearchException {
        SearchOutput output = BGG.search(searchTerm, ThingType.BOARDGAME);
        return  output.getItems();
    }

    @GetMapping(path = "/fetch/{ids}")
    public Collection<FetchItem> searchBgg(@PathVariable("ids") Integer[] ids) throws FetchException {
         return BGG.fetch(Arrays.asList(ids), ThingType.BOARDGAME);
    }
}
