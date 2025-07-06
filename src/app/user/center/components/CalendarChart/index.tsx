import React, { useEffect, useState, useRef } from "react";
import ReactECharts from "echarts-for-react";
import dayjs from "dayjs";
import { message, Typography, Row, Col, Card, Tooltip } from "antd";
import { getUserSignInRecordUsingGet } from "@/api/userController";
import "./index.css";
import { CheckCircleOutlined, FireOutlined, TrophyOutlined } from "@ant-design/icons";

const { Title, Text } = Typography;

interface Props {}

/**
 * 自定义统计数据卡片，替代Antd的Statistic组件，避免findDOMNode警告
 */
const CustomStatistic = ({ 
  title, 
  value, 
  prefix, 
  suffix, 
  valueStyle = { color: '#333' } 
}: { 
  title: React.ReactNode; 
  value: number | string; 
  prefix?: React.ReactNode; 
  suffix?: React.ReactNode; 
  valueStyle?: React.CSSProperties; 
}) => {
  return (
    <div className="custom-statistic">
      <div className="custom-statistic-title">{title}</div>
      <div className="custom-statistic-content">
        {prefix && <span className="custom-statistic-prefix">{prefix}</span>}
        <span className="custom-statistic-value" style={valueStyle}>{value}</span>
        {suffix && <span className="custom-statistic-suffix">{suffix}</span>}
      </div>
    </div>
  );
};

/**
 * 刷题日历图
 * @param props
 * @constructor
 */
const CalendarChart = (props: Props) => {
  const {} = props;

  // 签到日期列表（[1, 200]，表示第 1 和第 200 天有签到记录）
  const [dataList, setDataList] = useState<number[]>([]);
  // 当前年份
  const year = new Date().getFullYear();
  // 连续签到天数（模拟数据）
  const [continuousRecords, setContinuousRecords] = useState<number>(0);
  // 总签到天数
  const [totalRecords, setTotalRecords] = useState<number>(0);
  
  // 创建ref用于解决findDOMNode警告
  const achievementRef = useRef(null);

  // 请求后端获取数据
  const fetchDataList = async () => {
    try {
      const res = await getUserSignInRecordUsingGet({
        year,
      }) as any;
      
      if (res.data && Array.isArray(res.data)) {
      setDataList(res.data);
        // 计算总签到天数
        setTotalRecords(res.data.length);
        
        // 计算连续签到天数
        let maxContinuous = 0;
        let current = 0;
        
        // 对日期进行排序
        const sortedDates = [...res.data].sort((a, b) => a - b);
        
        for (let i = 0; i < sortedDates.length; i++) {
          if (i > 0 && sortedDates[i] === sortedDates[i-1] + 1) {
            // 连续日期
            current++;
          } else {
            // 不连续，重置计数
            current = 1;
          }
          
          // 更新最大连续天数
          maxContinuous = Math.max(maxContinuous, current);
        }
        
        setContinuousRecords(maxContinuous);
      }
    } catch (e) {
      message.error("获取刷题签到记录失败，" + (e as any).message);
    }
  };

  // 保证只会调用一次
  useEffect(() => {
    fetchDataList();
  }, []);

  // 计算图表所需的数据
  const optionsData = dataList.map((dayOfYear) => {
    // 计算日期字符串
    const dateStr = dayjs(`${year}-01-01`)
      .add(dayOfYear - 1, "day")
      .format("YYYY-MM-DD");
    return [dateStr, 1];
  });

  // 图表配置
  const options = {
    title: {
      text: `${year}年刷题记录`,
      left: 'center',
      textStyle: {
        color: '#333',
        fontSize: 16,
        fontWeight: 'normal'
      }
    },
    tooltip: {
      formatter: function (params) {
        const date = params.data[0];
        return `${date}<br/>已完成刷题`;
      }
    },
    visualMap: {
      show: false,
      min: 0,
      max: 1,
      inRange: {
        // 使用更鲜明的颜色方案
        color: ['#ebedf0', '#40a9ff'],
      },
    },
    calendar: {
      top: 50,
      range: year,
      left: 30,
      right: 30,
      // 单元格自动宽度，高度为 18 像素
      cellSize: ['auto', 18],
      itemStyle: {
        borderWidth: 1,
        borderColor: '#fff'
      },
      yearLabel: {
        show: false
      },
      dayLabel: {
        color: '#333',
        firstDay: 1, // 从周一开始
        nameMap: ['日', '一', '二', '三', '四', '五', '六']
      },
      monthLabel: {
        color: '#333',
        nameMap: 'cn'
      },
      splitLine: {
        show: true,
        lineStyle: {
          color: '#f0f0f0',
          width: 1,
          type: 'solid'
        }
      }
    },
    series: {
      type: "heatmap",
      coordinateSystem: "calendar",
      data: optionsData,
    },
  };

  return (
    <div className="calendar-chart-container">
      <Row gutter={[16, 16]} className="stats-row">
        <Col xs={24} sm={8}>
          <Card className="stat-card">
            <CustomStatistic 
              title={<Text className="stat-title">总刷题天数</Text>}
              value={totalRecords}
              prefix={<CheckCircleOutlined className="stat-icon check-icon" />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={8}>
          <Card className="stat-card">
            <CustomStatistic 
              title={<Text className="stat-title">连续刷题</Text>}
              value={continuousRecords}
              prefix={<FireOutlined className="stat-icon fire-icon" />}
              suffix="天"
            />
          </Card>
        </Col>
        <Col xs={24} sm={8}>
          <Card className="stat-card" ref={achievementRef}>
            <Tooltip title="完成当月目标可获得成就" getPopupContainer={() => achievementRef.current}>
              <div>
                <CustomStatistic 
                  title={<Text className="stat-title">本月目标</Text>}
                  value={Math.min(Math.round(totalRecords / 5), 100)}
                  prefix={<TrophyOutlined className="stat-icon trophy-icon" />}
                  suffix="%"
                />
              </div>
            </Tooltip>
          </Card>
        </Col>
      </Row>
      
      <div className="calendar-wrapper">
        <ReactECharts className="calendar-chart" option={options} />
      </div>
      
      <div className="calendar-legend">
        <Text type="secondary">活跃度: </Text>
        <div className="legend-item">
          <span className="legend-color legend-less"></span>
          <span className="legend-label">较少</span>
        </div>
        <div className="legend-item">
          <span className="legend-color legend-more"></span>
          <span className="legend-label">较多</span>
        </div>
      </div>
    </div>
  );
};

export default CalendarChart;