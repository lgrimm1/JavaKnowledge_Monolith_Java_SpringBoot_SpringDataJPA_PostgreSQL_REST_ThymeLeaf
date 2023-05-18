package lgrimm.javaknowledge.controller;

import lgrimm.javaknowledge.databasestorage.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;

import java.util.*;

@Configuration
public class Config {

	@Bean
	CommandLineRunner titleCommandLineRunner(TitleRepository repository) {
		return args -> {
			TitleEntity entity1 = new TitleEntity(
					"LOREM IPSUM DOLOR SIT AMET 1",
                    1L,
					1L);
			TitleEntity entity2 = new TitleEntity(
					"LOREM IPSUM DOLOR SIT AMET 2",
                    2L,
					2L);
			TitleEntity entity3 = new TitleEntity(
					"LOREM IPSUM DOLOR SIT AMET 3",
                    3L,
					3L);
			repository.saveAll(List.of(entity1, entity2, entity3));
		};
	}
	
	@Bean
	CommandLineRunner txtCommandLineRunner(TxtRepository repository) {
	String content1 = """
		1. LOREM IPSUM DOLOR SIT AMET
		2. LOREM IPSUM DOLOR SIT AMET
		3. LOREM IPSUM DOLOR SIT AMET
		
		=================================================================================
		1. LOREM IPSUM DOLOR SIT AMET
		=================================================================================
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam massa erat, pretium sed nibh vel, ornare finibus nulla. Donec elit dui, gravida at ante et, cursus semper mauris. Proin vitae posuere tortor. Donec id lectus at purus fringilla hendrerit. Vestibulum sed tincidunt augue. Sed euismod luctus tincidunt. In hac habitasse platea dictumst. Nulla dignissim enim in convallis dapibus. Maecenas sit amet eros nibh. Nam lectus ligula, tincidunt a sodales eu, laoreet vitae eros. Pellentesque in diam sit amet sem fringilla lacinia. Vestibulum odio lorem, ornare quis ex commodo, facilisis semper massa. Sed eu nunc ac urna suscipit eleifend ut at mi.
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem.
		
		Morbi at dolor sit amet nisi maximus auctor at a ante. Vivamus sagittis nunc in mi aliquet efficitur. Aliquam quis felis a quam lobortis consequat. Aenean risus nunc, ultrices aliquet diam cursus, ultrices rhoncus tortor. Aliquam erat volutpat. Quisque lorem dui, hendrerit in velit a, semper faucibus augue. In quis odio non nisl ornare vulputate. Donec eget metus ac neque sagittis vestibulum feugiat sit amet erat. Suspendisse sed rhoncus quam, vitae sagittis mauris.
		Praesent metus augue, accumsan et dignissim sed, mattis quis arcu. Praesent et tortor lectus. Vestibulum eu metus accumsan, molestie dolor vitae, venenatis purus. In in aliquet arcu, et posuere lorem. Vestibulum suscipit nunc quis ligula suscipit, non ultricies dolor condimentum. In hac habitasse platea dictumst. Curabitur ut diam quis risus auctor congue. Vivamus dapibus tellus nec massa rutrum, at vestibulum lectus aliquam. Nulla accumsan, erat vitae posuere euismod, justo lorem egestas mauris, id pharetra ante est sit amet nisi. Cras eget hendrerit purus. Praesent maximus enim vitae mi tristique semper. Duis commodo ac magna id commodo. Nulla pharetra molestie enim, et dictum odio varius nec. Mauris semper sed sapien ut molestie. Cras eu leo nec metus pretium euismod. Donec posuere, diam vel malesuada auctor, elit felis semper lectus, ac faucibus ex sem in justo.
		
		=================================================================================
		2. LOREM IPSUM DOLOR SIT AMET
		=================================================================================
		2.1. LOREM IPSUM DOLOR SIT AMET
		2.2. LOREM IPSUM DOLOR SIT AMET
		
		2.1. LOREM IPSUM DOLOR SIT AMET
		---------------------------------------------------------------------------------
		Donec euismod nisi ac enim elementum luctus in et nisi. Nulla scelerisque semper turpis id pulvinar. In semper quam fringilla tincidunt aliquet. Cras ac est dolor. Integer nec mi neque. Aliquam non erat ac augue consectetur ultrices. Nam vel pharetra lectus. Duis sit amet ligula augue. Sed lobortis tortor eu leo imperdiet dapibus. Donec sagittis vehicula urna vitae placerat. Nulla pulvinar mollis tincidunt. Proin id lectus nec ante dignissim ultricies.
		Sed lorem turpis, gravida vitae sem sed, volutpat tincidunt velit.
		Proin porta laoreet augue, et interdum metus hendrerit sit amet. Vestibulum eleifend nisi eget metus finibus tempor.
		Sed nec libero cursus, elementum urna non, pharetra tellus. Maecenas ante metus, volutpat et felis vitae, fermentum mattis orci. Sed tincidunt nisl libero, sagittis volutpat dui pharetra vel. Mauris ultrices pellentesque erat vel molestie. In hac habitasse platea dictumst. In feugiat mi libero, sed lobortis ipsum vestibulum sed. Nunc consectetur hendrerit risus, non rutrum urna dictum in. Phasellus varius, felis consectetur porttitor pharetra, arcu diam condimentum enim, sit amet venenatis magna lectus eu est.
		
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam massa erat, pretium sed nibh vel, ornare finibus nulla. Donec elit dui, gravida at ante et, cursus semper mauris. Proin vitae posuere tortor. Donec id lectus at purus fringilla hendrerit. Vestibulum sed tincidunt augue. Sed euismod luctus tincidunt. In hac habitasse platea dictumst. Nulla dignissim enim in convallis dapibus. Maecenas sit amet eros nibh. Nam lectus ligula, tincidunt a sodales eu, laoreet vitae eros. Pellentesque in diam sit amet sem fringilla lacinia. Vestibulum odio lorem, ornare quis ex commodo, facilisis semper massa. Sed eu nunc ac urna suscipit eleifend ut at mi.
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem.
		
		2.2. LOREM IPSUM DOLOR SIT AMET
		---------------------------------------------------------------------------------
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam massa erat, pretium sed nibh vel, ornare finibus nulla. Donec elit dui, gravida at ante et, cursus semper mauris. Proin vitae posuere tortor. Donec id lectus at purus fringilla hendrerit. Vestibulum sed tincidunt augue. Sed euismod luctus tincidunt. In hac habitasse platea dictumst. Nulla dignissim enim in convallis dapibus. Maecenas sit amet eros nibh. Nam lectus ligula, tincidunt a sodales eu, laoreet vitae eros. Pellentesque in diam sit amet sem fringilla lacinia. Vestibulum odio lorem, ornare quis ex commodo, facilisis semper massa. Sed eu nunc ac urna suscipit eleifend ut at mi.
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem. Morbi at dolor sit amet nisi maximus auctor at a ante. Vivamus sagittis nunc in mi aliquet efficitur. Aliquam quis felis a quam lobortis consequat. Aenean risus nunc, ultrices aliquet diam cursus, ultrices rhoncus tortor. Aliquam erat volutpat. Quisque lorem dui, hendrerit in velit a, semper faucibus augue. In quis odio non nisl ornare vulputate. Donec eget metus ac neque sagittis vestibulum feugiat sit amet erat. Suspendisse sed rhoncus quam, vitae sagittis mauris.
		Praesent metus augue, accumsan et dignissim sed, mattis quis arcu. Praesent et tortor lectus. Vestibulum eu metus accumsan, molestie dolor vitae, venenatis purus. In in aliquet arcu, et posuere lorem. Vestibulum suscipit nunc quis ligula suscipit, non ultricies dolor condimentum. In hac habitasse platea dictumst. Curabitur ut diam quis risus auctor congue. Vivamus dapibus tellus nec massa rutrum, at vestibulum lectus aliquam. Nulla accumsan, erat vitae posuere euismod, justo lorem egestas mauris, id pharetra ante est sit amet nisi. Cras eget hendrerit purus. Praesent maximus enim vitae mi tristique semper. Duis commodo ac magna id commodo. Nulla pharetra molestie enim, et dictum odio varius nec. Mauris semper sed sapien ut molestie. Cras eu leo nec metus pretium euismod. Donec posuere, diam vel malesuada auctor, elit felis semper lectus, ac faucibus ex sem in justo.
		Donec euismod nisi ac enim elementum luctus in et nisi. Nulla scelerisque semper turpis id pulvinar. In semper quam fringilla tincidunt aliquet. Cras ac est dolor. Integer nec mi neque. Aliquam non erat ac augue consectetur ultrices. Nam vel pharetra lectus. Duis sit amet ligula augue. Sed lobortis tortor eu leo imperdiet dapibus. Donec sagittis vehicula urna vitae placerat. Nulla pulvinar mollis tincidunt. Proin id lectus nec ante dignissim ultricies.
		
		=================================================================================
		3. LOREM IPSUM DOLOR SIT AMET
		=================================================================================
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam massa erat, pretium sed nibh vel, ornare finibus nulla. Donec elit dui, gravida at ante et, cursus semper mauris. Proin vitae posuere tortor. Donec id lectus at purus fringilla hendrerit. Vestibulum sed tincidunt augue. Sed euismod luctus tincidunt. In hac habitasse platea dictumst. Nulla dignissim enim in convallis dapibus. Maecenas sit amet eros nibh. Nam lectus ligula, tincidunt a sodales eu, laoreet vitae eros. Pellentesque in diam sit amet sem fringilla lacinia. Vestibulum odio lorem, ornare quis ex commodo, facilisis semper massa. Sed eu nunc ac urna suscipit eleifend ut at mi.
		
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem. Morbi at dolor sit amet nisi maximus auctor at a ante. Vivamus sagittis nunc in mi aliquet efficitur. Aliquam quis felis a quam lobortis consequat. Aenean risus nunc, ultrices aliquet diam cursus, ultrices rhoncus tortor. Aliquam erat volutpat. Quisque lorem dui, hendrerit in velit a, semper faucibus augue. In quis odio non nisl ornare vulputate. Donec eget metus ac neque sagittis vestibulum feugiat sit amet erat. Suspendisse sed rhoncus quam, vitae sagittis mauris.
		
		Praesent metus augue, accumsan et dignissim sed, mattis quis arcu. Praesent et tortor lectus. Vestibulum eu metus accumsan, molestie dolor vitae, venenatis purus. In in aliquet arcu, et posuere lorem. Vestibulum suscipit nunc quis ligula suscipit, non ultricies dolor condimentum. In hac habitasse platea dictumst. Curabitur ut diam quis risus auctor congue. Vivamus dapibus tellus nec massa rutrum, at vestibulum lectus aliquam. Nulla accumsan, erat vitae posuere euismod, justo lorem egestas mauris, id pharetra ante est sit amet nisi. Cras eget hendrerit purus. Praesent maximus enim vitae mi tristique semper. Duis commodo ac magna id commodo. Nulla pharetra molestie enim, et dictum odio varius nec. Mauris semper sed sapien ut molestie. Cras eu leo nec metus pretium euismod. Donec posuere, diam vel malesuada auctor, elit felis semper lectus, ac faucibus ex sem in justo.
		Donec euismod nisi ac enim elementum luctus in et nisi. Nulla scelerisque semper turpis id pulvinar. In semper quam fringilla tincidunt aliquet. Cras ac est dolor. Integer nec mi neque. Aliquam non erat ac augue consectetur ultrices. Nam vel pharetra lectus. Duis sit amet ligula augue. Sed lobortis tortor eu leo imperdiet dapibus. Donec sagittis vehicula urna vitae placerat. Nulla pulvinar mollis tincidunt. Proin id lectus nec ante dignissim ultricies.
		
		=>LOREM IPSUM DOLOR SIT AMET 2
		=>LOREM IPSUM DOLOR SIT AMET 3
			""";
	String content2 = """
		Lorem ipsum dolor sit amet.
		Consectetur adipiscing elit. Aliquam massa erat, pretium sed nibh vel.
		Ornare finibus nulla:
			- Donec elit dui, gravida at ante et (cursus semper mauris)
		    - Proin vitae posuere tortor
			- Donec elit dui, gravida at ante et (cursus semper mauris)
		    - Proin vitae posuere tortor
			- Donec elit dui, gravida at ante et (cursus semper mauris)
		    - Proin vitae posuere tortor
		
		Ornare finibus nulla:
			- Donec elit dui, gravida at ante et (cursus semper mauris)
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
		    - Proin vitae posuere tortor
			- Donec elit dui, gravida at ante et (cursus semper mauris)
			- Donec elit dui, gravida at ante et (cursus semper mauris)
			- Donec elit dui, gravida at ante et (cursus semper mauris)
			- Donec elit dui, gravida at ante et (cursus semper mauris)
			- Donec elit dui, gravida at ante et (cursus semper mauris)
			- Donec elit dui, gravida at ante et (cursus semper mauris)
			- Donec elit dui, gravida at ante et (cursus semper mauris)
			- Donec elit dui, gravida at ante et (cursus semper mauris)
			- Donec elit dui, gravida at ante et (cursus semper mauris)
		    - Proin vitae posuere tortor
			- Donec elit dui, gravida at ante et (cursus semper mauris)
		    - Proin vitae posuere tortor
			- Donec elit dui, gravida at ante et (cursus semper mauris)
		    - Proin vitae posuere tortor
			- Donec elit dui, gravida at ante et (cursus semper mauris)
		Donec id lectus at purus fringilla hendrerit. Vestibulum sed tincidunt augue.
		
		Sed euismod luctus tincidunt. In hac habitasse platea dictumst. Nulla dignissim enim in convallis dapibus. Maecenas sit amet eros nibh. Nam lectus ligula, tincidunt a sodales eu, laoreet vitae eros. Pellentesque in diam sit amet sem fringilla lacinia. Vestibulum odio lorem, ornare quis ex commodo, facilisis semper massa. Sed eu nunc ac urna suscipit eleifend ut at mi.
		Praesent metus augue, accumsan et dignissim sed, mattis quis arcu. Praesent et tortor lectus. Vestibulum eu metus accumsan, molestie dolor vitae, venenatis purus. In in aliquet arcu, et posuere lorem. Vestibulum suscipit nunc quis ligula suscipit, non ultricies dolor condimentum. In hac habitasse platea dictumst. Curabitur ut diam quis risus auctor congue. Vivamus dapibus tellus nec massa rutrum, at vestibulum lectus aliquam. Nulla accumsan, erat vitae posuere euismod, justo lorem egestas mauris, id pharetra ante est sit amet nisi. Cras eget hendrerit purus. Praesent maximus enim vitae mi tristique semper. Duis commodo ac magna id commodo. Nulla pharetra molestie enim, et dictum odio varius nec. Mauris semper sed sapien ut molestie. Cras eu leo nec metus pretium euismod. Donec posuere, diam vel malesuada auctor, elit felis semper lectus, ac faucibus ex sem in justo.
		
		Lorem ipsum dolor sit amet, consectetur adipiscing elit.
		
		||Aliquam massa erat|Pretium sed nibh vel|Ornare finibus||
		||nulla|donec elit dui, gravida at ante et|cursus semper mauris||
		||proin vitae|posuere tortor (donec id lectus)|at purus fringilla hendrerit
		||vestibulum sed tincidunt augue. Sed euismod luctus|tincidunt|hac habitasse||
		||platea dictumst, nulla|dignissim enim in convallis|dapibus||
		||maecenas sit amet eros nibh|nam|lectus ligula, tincidunt a sodales eu, laoreet vitae eros, pellentesque in diam sit amet||
		
		Lorem ipsum dolor sit amet, consectetur adipiscing elit.
		
		||Aliquam massa erat|Pretium sed nibh vel|Ornare finibus||
		||nulla|donec elit dui, gravida at ante et|cursus semper mauris||
		
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem. Morbi at dolor sit amet nisi maximus auctor at a ante. Vivamus sagittis nunc in mi aliquet efficitur. Aliquam quis felis a quam lobortis consequat.
		
		EXAMPLE FOR LOREM IPSUM DOLOR SIT AMET (CONSECTETUR ADIPISCING ELIT):
		lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
				lorem ipsum dolor sit amet
				lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		END OF EXAMPLE
		
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem. Morbi at dolor sit amet nisi maximus auctor at a ante. Vivamus sagittis nunc in mi aliquet efficitur. Aliquam quis felis a quam lobortis consequat.
		
		EXAMPLE FOR LOREM IPSUM DOLOR SIT AMET (CONSECTETUR ADIPISCING ELIT):
		lorem ipsum dolor sit amet
		    lorem ipsum dolor sit amet
		    lorem ipsum dolor sit amet
		        lorem ipsum dolor sit amet
		        lorem ipsum dolor sit amet
		    lorem ipsum dolor sit amet
		    lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		END OF EXAMPLE
		
		MORE HERE: Lorem ipsum dolor sit amet
		MORE HERE: Lorem ipsum dolor sit amet
		MORE HERE: Lorem ipsum dolor sit amet
		
		=>LOREM IPSUM DOLOR SIT AMET 1
		=>LOREM IPSUM DOLOR SIT AMET 3
			""";
	String content3 = """
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam massa erat, pretium sed nibh vel, ornare finibus nulla. Donec elit dui, gravida at ante et, cursus semper mauris. Proin vitae posuere tortor. Donec id lectus at purus fringilla hendrerit. Vestibulum sed tincidunt augue. Sed euismod luctus tincidunt. In hac habitasse platea dictumst. Nulla dignissim enim in convallis dapibus. Maecenas sit amet eros nibh. Nam lectus ligula, tincidunt a sodales eu, laoreet vitae eros. Pellentesque in diam sit amet sem fringilla lacinia. Vestibulum odio lorem, ornare quis ex commodo, facilisis semper massa. Sed eu nunc ac urna suscipit eleifend ut at mi.
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem.
		
		Morbi at dolor sit amet nisi maximus auctor at a ante. Vivamus sagittis nunc in mi aliquet efficitur. Aliquam quis felis a quam lobortis consequat. Aenean risus nunc, ultrices aliquet diam cursus, ultrices rhoncus tortor. Aliquam erat volutpat. Quisque lorem dui, hendrerit in velit a, semper faucibus augue. In quis odio non nisl ornare vulputate. Donec eget metus ac neque sagittis vestibulum feugiat sit amet erat. Suspendisse sed rhoncus quam, vitae sagittis mauris.
		Praesent metus augue, accumsan et dignissim sed, mattis quis arcu. Praesent et tortor lectus. Vestibulum eu metus accumsan, molestie dolor vitae, venenatis purus. In in aliquet arcu, et posuere lorem. Vestibulum suscipit nunc quis ligula suscipit, non ultricies dolor condimentum. In hac habitasse platea dictumst. Curabitur ut diam quis risus auctor congue. Vivamus dapibus tellus nec massa rutrum, at vestibulum lectus aliquam. Nulla accumsan, erat vitae posuere euismod, justo lorem egestas mauris, id pharetra ante est sit amet nisi. Cras eget hendrerit purus. Praesent maximus enim vitae mi tristique semper. Duis commodo ac magna id commodo. Nulla pharetra molestie enim, et dictum odio varius nec. Mauris semper sed sapien ut molestie. Cras eu leo nec metus pretium euismod. Donec posuere, diam vel malesuada auctor, elit felis semper lectus, ac faucibus ex sem in justo.
		
		Donec euismod nisi ac enim elementum luctus in et nisi. Nulla scelerisque semper turpis id pulvinar. In semper quam fringilla tincidunt aliquet. Cras ac est dolor. Integer nec mi neque. Aliquam non erat ac augue consectetur ultrices. Nam vel pharetra lectus. Duis sit amet ligula augue. Sed lobortis tortor eu leo imperdiet dapibus. Donec sagittis vehicula urna vitae placerat. Nulla pulvinar mollis tincidunt. Proin id lectus nec ante dignissim ultricies.
		Sed lorem turpis, gravida vitae sem sed, volutpat tincidunt velit.
		Proin porta laoreet augue, et interdum metus hendrerit sit amet. Vestibulum eleifend nisi eget metus finibus tempor.
		Sed nec libero cursus, elementum urna non, pharetra tellus. Maecenas ante metus, volutpat et felis vitae, fermentum mattis orci. Sed tincidunt nisl libero, sagittis volutpat dui pharetra vel. Mauris ultrices pellentesque erat vel molestie. In hac habitasse platea dictumst. In feugiat mi libero, sed lobortis ipsum vestibulum sed. Nunc consectetur hendrerit risus, non rutrum urna dictum in. Phasellus varius, felis consectetur porttitor pharetra, arcu diam condimentum enim, sit amet venenatis magna lectus eu est.
		
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam massa erat, pretium sed nibh vel, ornare finibus nulla. Donec elit dui, gravida at ante et, cursus semper mauris. Proin vitae posuere tortor. Donec id lectus at purus fringilla hendrerit. Vestibulum sed tincidunt augue. Sed euismod luctus tincidunt. In hac habitasse platea dictumst. Nulla dignissim enim in convallis dapibus. Maecenas sit amet eros nibh. Nam lectus ligula, tincidunt a sodales eu, laoreet vitae eros. Pellentesque in diam sit amet sem fringilla lacinia. Vestibulum odio lorem, ornare quis ex commodo, facilisis semper massa. Sed eu nunc ac urna suscipit eleifend ut at mi.
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem.
		
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam massa erat, pretium sed nibh vel, ornare finibus nulla. Donec elit dui, gravida at ante et, cursus semper mauris. Proin vitae posuere tortor. Donec id lectus at purus fringilla hendrerit. Vestibulum sed tincidunt augue. Sed euismod luctus tincidunt. In hac habitasse platea dictumst. Nulla dignissim enim in convallis dapibus. Maecenas sit amet eros nibh. Nam lectus ligula, tincidunt a sodales eu, laoreet vitae eros. Pellentesque in diam sit amet sem fringilla lacinia. Vestibulum odio lorem, ornare quis ex commodo, facilisis semper massa. Sed eu nunc ac urna suscipit eleifend ut at mi.
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem. Morbi at dolor sit amet nisi maximus auctor at a ante. Vivamus sagittis nunc in mi aliquet efficitur. Aliquam quis felis a quam lobortis consequat. Aenean risus nunc, ultrices aliquet diam cursus, ultrices rhoncus tortor. Aliquam erat volutpat. Quisque lorem dui, hendrerit in velit a, semper faucibus augue. In quis odio non nisl ornare vulputate. Donec eget metus ac neque sagittis vestibulum feugiat sit amet erat. Suspendisse sed rhoncus quam, vitae sagittis mauris.
		Praesent metus augue, accumsan et dignissim sed, mattis quis arcu. Praesent et tortor lectus. Vestibulum eu metus accumsan, molestie dolor vitae, venenatis purus. In in aliquet arcu, et posuere lorem. Vestibulum suscipit nunc quis ligula suscipit, non ultricies dolor condimentum. In hac habitasse platea dictumst. Curabitur ut diam quis risus auctor congue. Vivamus dapibus tellus nec massa rutrum, at vestibulum lectus aliquam. Nulla accumsan, erat vitae posuere euismod, justo lorem egestas mauris, id pharetra ante est sit amet nisi. Cras eget hendrerit purus. Praesent maximus enim vitae mi tristique semper. Duis commodo ac magna id commodo. Nulla pharetra molestie enim, et dictum odio varius nec. Mauris semper sed sapien ut molestie. Cras eu leo nec metus pretium euismod. Donec posuere, diam vel malesuada auctor, elit felis semper lectus, ac faucibus ex sem in justo.
		Donec euismod nisi ac enim elementum luctus in et nisi. Nulla scelerisque semper turpis id pulvinar. In semper quam fringilla tincidunt aliquet. Cras ac est dolor. Integer nec mi neque. Aliquam non erat ac augue consectetur ultrices. Nam vel pharetra lectus. Duis sit amet ligula augue. Sed lobortis tortor eu leo imperdiet dapibus. Donec sagittis vehicula urna vitae placerat. Nulla pulvinar mollis tincidunt. Proin id lectus nec ante dignissim ultricies.
		
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam massa erat, pretium sed nibh vel, ornare finibus nulla. Donec elit dui, gravida at ante et, cursus semper mauris. Proin vitae posuere tortor. Donec id lectus at purus fringilla hendrerit. Vestibulum sed tincidunt augue. Sed euismod luctus tincidunt. In hac habitasse platea dictumst. Nulla dignissim enim in convallis dapibus. Maecenas sit amet eros nibh. Nam lectus ligula, tincidunt a sodales eu, laoreet vitae eros. Pellentesque in diam sit amet sem fringilla lacinia. Vestibulum odio lorem, ornare quis ex commodo, facilisis semper massa. Sed eu nunc ac urna suscipit eleifend ut at mi.
		
		Pellentesque eget dui rhoncus, malesuada orci sollicitudin, fringilla arcu. Maecenas quis felis convallis, dapibus ex non, cursus sem. Morbi at dolor sit amet nisi maximus auctor at a ante. Vivamus sagittis nunc in mi aliquet efficitur. Aliquam quis felis a quam lobortis consequat. Aenean risus nunc, ultrices aliquet diam cursus, ultrices rhoncus tortor. Aliquam erat volutpat. Quisque lorem dui, hendrerit in velit a, semper faucibus augue. In quis odio non nisl ornare vulputate. Donec eget metus ac neque sagittis vestibulum feugiat sit amet erat. Suspendisse sed rhoncus quam, vitae sagittis mauris.
		
		Praesent metus augue, accumsan et dignissim sed, mattis quis arcu. Praesent et tortor lectus. Vestibulum eu metus accumsan, molestie dolor vitae, venenatis purus. In in aliquet arcu, et posuere lorem. Vestibulum suscipit nunc quis ligula suscipit, non ultricies dolor condimentum. In hac habitasse platea dictumst. Curabitur ut diam quis risus auctor congue. Vivamus dapibus tellus nec massa rutrum, at vestibulum lectus aliquam. Nulla accumsan, erat vitae posuere euismod, justo lorem egestas mauris, id pharetra ante est sit amet nisi. Cras eget hendrerit purus. Praesent maximus enim vitae mi tristique semper. Duis commodo ac magna id commodo. Nulla pharetra molestie enim, et dictum odio varius nec. Mauris semper sed sapien ut molestie. Cras eu leo nec metus pretium euismod. Donec posuere, diam vel malesuada auctor, elit felis semper lectus, ac faucibus ex sem in justo.
		Donec euismod nisi ac enim elementum luctus in et nisi. Nulla scelerisque semper turpis id pulvinar. In semper quam fringilla tincidunt aliquet. Cras ac est dolor. Integer nec mi neque. Aliquam non erat ac augue consectetur ultrices. Nam vel pharetra lectus. Duis sit amet ligula augue. Sed lobortis tortor eu leo imperdiet dapibus. Donec sagittis vehicula urna vitae placerat. Nulla pulvinar mollis tincidunt. Proin id lectus nec ante dignissim ultricies.
		
		EXAMPLE FOR LOREM IPSUM DOLOR SIT AMET (CONSECTETUR ADIPISCING ELIT):
		lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		END OF EXAMPLE
		
		EXAMPLE FOR LOREM IPSUM DOLOR SIT AMET (CONSECTETUR ADIPISCING ELIT):
		lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		END OF EXAMPLE
		
		EXAMPLE FOR LOREM IPSUM DOLOR SIT AMET (CONSECTETUR ADIPISCING ELIT):
		lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		END OF EXAMPLE
		
		EXAMPLE FOR LOREM IPSUM DOLOR SIT AMET (CONSECTETUR ADIPISCING ELIT):
		lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		END OF EXAMPLE
		
		EXAMPLE FOR LOREM IPSUM DOLOR SIT AMET (CONSECTETUR ADIPISCING ELIT):
		lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		END OF EXAMPLE
		
		EXAMPLE FOR LOREM IPSUM DOLOR SIT AMET (CONSECTETUR ADIPISCING ELIT):
		lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
			lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		lorem ipsum dolor sit amet
		END OF EXAMPLE
		
		=>LOREM IPSUM DOLOR SIT AMET 1
		=>LOREM IPSUM DOLOR SIT AMET 2
			""";
		return args -> {
			TxtEntity entity1 = new TxtEntity(content1);
			TxtEntity entity2 = new TxtEntity(content2);
			TxtEntity entity3 = new TxtEntity(content3);
			repository.saveAll(List.of(entity1, entity2, entity3));
		};
	}

	@Bean
	CommandLineRunner htmlCommandLineRunner(HtmlRepository repository) {
		return args -> {
			HtmlEntity entity1 = new HtmlEntity("", "");
			HtmlEntity entity2 = new HtmlEntity("", "");
			HtmlEntity entity3 = new HtmlEntity("", "");
			repository.saveAll(List.of(entity1, entity2, entity3));
		};
	}
}
