require ["barchart", "wordcloud", "wordcount"], (chart, cloud, wordCount) ->

  lastCloudUpdate = new Date().getTime() - 2000
  lastBarUpdate = new Date().getTime() - 2000
  showTweets = 10
  
  handler = (msg) ->
    tweet = JSON.parse(msg.data)
    wordCount.insert [tweet]
    viewModel.tweets.unshift tweet
    viewModel.tweets.pop()
    if (new Date().getTime() - lastBarUpdate) > 800
      barchart.redraw wordCount.getWords()
      lastBarUpdate = new Date().getTime()
    if (new Date().getTime() - lastCloudUpdate) > 5000
      wordCloud.redraw wordCount.getWords()
      lastCloudUpdate = new Date().getTime()

  feed = new EventSource("/tweetFeed")
  feed.addEventListener "message", handler, false
  
  class TweetsViewModel
    constructor: -> @tweets = ko.observableArray()
      
  viewModel = new TweetsViewModel
  ko.applyBindings viewModel
  barchart = chart.BarChart()
  wordCloud = cloud.WordCloud(700, 500, 250)
  wordCount = wordCount.WordCount()

  d3.json "/tweets/latest?n=1000", (tweets) ->
    wordCount.insert tweets
    barchart.redraw wordCount.getWords()
    wordCloud.redraw wordCount.getWords()    
    tweetsShort = tweets.slice(0, showTweets).reverse()
    viewModel.tweets.unshift t for t in tweetsShort
    